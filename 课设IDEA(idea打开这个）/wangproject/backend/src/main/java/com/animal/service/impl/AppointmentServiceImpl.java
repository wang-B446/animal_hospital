package com.animal.service.impl;

import com.animal.common.PageResult;
import com.animal.entity.Appointment;
import com.animal.mapper.AppointmentMapper;
import com.animal.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public List<Appointment> findAll() {
        return appointmentMapper.findAll();
    }

    @Override
    public Appointment findById(Integer id) {
        return appointmentMapper.findById(id);
    }

    @Override
    public boolean save(Appointment appointment) {
        if (appointment.getStatus() == null) {
            appointment.setStatus("待确认");
        }
        return appointmentMapper.insert(appointment) > 0;
    }

    @Override
    public boolean update(Appointment appointment) {
        return appointmentMapper.update(appointment) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return appointmentMapper.deleteById(id) > 0;
    }

    @Override
    public PageResult<Appointment> findByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Appointment> list = appointmentMapper.findByPage(offset, pageSize);
        long total = appointmentMapper.countAll();
        return new PageResult<>(list, total, pageNum, pageSize);
    }
}
