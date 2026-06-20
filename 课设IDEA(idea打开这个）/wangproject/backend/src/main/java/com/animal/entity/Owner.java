package com.animal.entity;

import lombok.Data;

@Data
public class Owner {
    private Integer id;
    private String name;
    private String phone;
    private String address;
    private String remark;
    private String createTime;
}
