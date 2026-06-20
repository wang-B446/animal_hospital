package com.animal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.animal.mapper")
public class AnimalHospitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnimalHospitalApplication.class, args);
    }
}
