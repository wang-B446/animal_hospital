package com.animal.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class StatVO {
    // 统计数字
    private Long animalCount;
    private Long ownerCount;
    private Long doctorCount;
    private Long recordCount;
    private BigDecimal totalIncome;
    private Long todayRecordCount;
    private BigDecimal todayIncome;

    // 种类分布
    private List<SpeciesStat> speciesStats;

    // 月度趋势
    private List<MonthStat> monthStats;

    @Data
    public static class SpeciesStat {
        private String name;
        private Long value;
    }

    @Data
    public static class MonthStat {
        private String month;
        private Long count;
        private BigDecimal income;
    }
}
