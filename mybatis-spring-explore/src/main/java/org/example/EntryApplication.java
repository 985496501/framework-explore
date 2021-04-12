package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringBoot Entry.
 *
 * @author: jinyun
 * @date: 2021/2/8
 */
@RestController
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class EntryApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntryApplication.class, args);
    }
}
