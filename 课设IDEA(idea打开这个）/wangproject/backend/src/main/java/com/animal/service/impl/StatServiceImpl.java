package com.animal.service.impl;

import com.animal.entity.StatVO;
import com.animal.mapper.AnimalMapper;
import com.animal.mapper.MedicalRecordMapper;
import com.animal.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public StatVO getStatistics() {
        StatVO stat = new StatVO();

        // 基础统计
        stat.setAnimalCount(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM animal", Long.class));
        stat.setOwnerCount(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM owner", Long.class));
        stat.setDoctorCount(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM doctor", Long.class));
        stat.setRecordCount(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM medical_record", Long.class));

        // 总收入（已缴费的）
        Object totalIncome = jdbcTemplate.queryForObject(
            "SELECT COALESCE(SUM(cost), 0) FROM medical_record WHERE payment_status = '已缴费'", BigDecimal.class);
        stat.setTotalIncome(totalIncome != null ? (BigDecimal) totalIncome : BigDecimal.ZERO);

        // 今日统计
        stat.setTodayRecordCount(jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM medical_record WHERE DATE(visit_date) = CURDATE()", Long.class));

        Object todayIncome = jdbcTemplate.queryForObject(
            "SELECT COALESCE(SUM(cost), 0) FROM medical_record WHERE payment_status = '已缴费' AND DATE(visit_date) = CURDATE()",
            BigDecimal.class);
        stat.setTodayIncome(todayIncome != null ? (BigDecimal) todayIncome : BigDecimal.ZERO);

        // 种类分布
        List<Map<String, Object>> speciesRows = jdbcTemplate.queryForList(
            "SELECT species AS name, COUNT(*) AS value FROM animal GROUP BY species");
        List<StatVO.SpeciesStat> speciesStats = speciesRows.stream().map(row -> {
            StatVO.SpeciesStat s = new StatVO.SpeciesStat();
            s.setName((String) row.get("name"));
            s.setValue(((Number) row.get("value")).longValue());
            return s;
        }).collect(Collectors.toList());
        stat.setSpeciesStats(speciesStats);

        // 月度趋势（近6个月）
        List<Map<String, Object>> monthRows = jdbcTemplate.queryForList(
            "SELECT DATE_FORMAT(visit_date, '%Y-%m') AS month, COUNT(*) AS count, COALESCE(SUM(cost), 0) AS income " +
            "FROM medical_record WHERE visit_date >= DATE_SUB(CURDATE(), INTERVAL 5 MONTH) " +
            "GROUP BY DATE_FORMAT(visit_date, '%Y-%m') ORDER BY month");
        List<StatVO.MonthStat> monthStats = monthRows.stream().map(row -> {
            StatVO.MonthStat m = new StatVO.MonthStat();
            m.setMonth((String) row.get("month"));
            m.setCount(((Number) row.get("count")).longValue());
            m.setIncome((BigDecimal) row.get("income"));
            return m;
        }).collect(Collectors.toList());
        stat.setMonthStats(monthStats);

        return stat;
    }
}
