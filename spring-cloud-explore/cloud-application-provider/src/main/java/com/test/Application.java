package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBootApplication
 * EnableDiscoveryClient
 * EnableCircuitBreaker
 *
 *
 * @author: jinyun
 * @date: 2021/3/30
 */
//@SpringCloudApplication
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
