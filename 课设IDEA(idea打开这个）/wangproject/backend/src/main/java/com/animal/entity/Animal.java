package com.animal.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Animal {
    private Integer id;
    private String name;
    private String species;
    private String breed;
    private String gender;
    private Integer age;
    private BigDecimal weight;
    private Integer ownerId;
    private String symptom;
    private String photo; // 照片路径
    private String vaccineDate; // 最近疫苗日期
    private String nextVaccineDate; // 下次疫苗日期
    private String createTime;
    // 关联查询字段
    private String ownerName;
}
