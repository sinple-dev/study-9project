package com.fastcampus.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class FastcompusApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastcompusApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
