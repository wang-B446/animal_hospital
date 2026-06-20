package com.animal.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLog {
    private Integer id;
    private String username;
    private String operation;
    private String module;
    private String method;
    private String params;
    private String ip;
    private LocalDateTime createTime;
}
