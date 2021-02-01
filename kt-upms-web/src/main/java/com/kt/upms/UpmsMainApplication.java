package com.kt.upms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.kt.upms.module.*.persistence.dao")
@SpringBootApplication
public class UpmsMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpmsMainApplication.class, args);
    }

}