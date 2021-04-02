package com.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
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
public class Application implements CommandLineRunner {
    @Value("${author}")
    private String value;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("============================" + value);
    }
}
