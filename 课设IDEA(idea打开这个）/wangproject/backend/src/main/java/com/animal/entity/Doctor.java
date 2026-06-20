package com.animal.entity;

import lombok.Data;

@Data
public class Doctor {
    private Integer id;
    private String name;
    private String title;
    private String specialty;
    private String phone;
    private String createTime;
}
