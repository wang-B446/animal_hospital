package com.animal.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Appointment {
    private Integer id;
    private Integer animalId;
    private Integer doctorId;
    private String appointmentDate;
    private String appointmentTime;
    private String reason;
    private String status; // 待确认/已确认/已取消/已完成
    private String createTime;
    // 动物信息（键盘输入）
    private String animalName;
    private String species;
    // 关联查询字段
    private String ownerName;
    private String doctorName;
}
