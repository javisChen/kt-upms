package com.kt.upms;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication(scanBasePackages = "com.kt.*")
@ComponentScan(basePackages = "com.kt.*")
@MapperScan(basePackages = "com.kt.upms.**.mapper")
public class KtUpmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KtUpmsApplication.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer(){
        return (Jackson2ObjectMapperBuilder builder) -> {
            builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        };
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
//        return objectMapper;
//    }

}
