package com.kt.upms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.kt.*")
@ComponentScan(basePackages = "com.kt.*")
@MapperScan(basePackages = "com.kt.upms.**.mapper")
public class KtUpmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KtUpmsApplication.class, args);
    }

}
