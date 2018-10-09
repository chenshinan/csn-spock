package com.chenshinan.spock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.chenshinan.spock.infra.mapper")
public class CsnSpockApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsnSpockApplication.class, args);
    }
}
