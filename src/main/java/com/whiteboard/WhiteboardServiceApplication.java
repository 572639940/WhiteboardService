package com.whiteboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.whiteboard.dao")
public class WhiteboardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhiteboardServiceApplication.class, args);
    }

}
