package org.example;

import org.example.client.RadicalClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringBoot Entry.
 *
 *
 *
 * @author: jinyun
 * @date: 2021/2/8
 */
@RequestMapping("main")
@RestController
@SpringBootApplication
public class EntryApplication {
    public static void main(String[] args) {
        SpringApplication.run(EntryApplication.class, args);
    }

    @Autowired
    private RadicalClient radicalClient;

    @GetMapping
    public String getMsg() {
        return radicalClient.exchange();
    }


    @Test
    public void classNameTest() {
        String name = EntryApplication.class.getName();
        System.out.println(name);
        // org.example.EntryApplication
    }
}
