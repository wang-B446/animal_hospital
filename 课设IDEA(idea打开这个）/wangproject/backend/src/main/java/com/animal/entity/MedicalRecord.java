package com.animal.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MedicalRecord {
    private Integer id;
    private Integer animalId;
    private Integer doctorId;
    private String visitDate;
    private String symptom;
    private String diagnosis;
    private String treatment;
    private BigDecimal cost;
    private String paymentStatus; // 未缴费/已缴费
    private String createTime;
    // 关联查询字段
    private String animalName;
    private String doctorName;
    private String ownerName;
}
