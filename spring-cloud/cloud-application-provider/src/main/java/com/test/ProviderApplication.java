package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringBootApplication
 * EnableDiscoveryClient
 * EnableCircuitBreaker
 *
 *
 * @author: jinyun
 * @date: 2021/3/30
 */
@RestController
@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @GetMapping("hello")
    public String getMethod() {
        return "hello world";
    }
}
