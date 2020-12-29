package com.kt.upms;

import com.kt.component.web.advice.PageRequestRequestBodyAdvice;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

@MapperScan(basePackages = "com.kt.upms.mapper")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RequestBodyAdvice pageRequestRequestBodyAdvice() {
        return new PageRequestRequestBodyAdvice();
    }

}