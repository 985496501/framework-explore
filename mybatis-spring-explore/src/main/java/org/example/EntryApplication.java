package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.Arrays;
import java.util.concurrent.locks.LockSupport;

/**
 * SpringBoot Entry.
 *
 * @author: jinyun
 * @date: 2021/2/8
 */

@EnableWebSocket
@RestController
@SpringBootApplication
public class EntryApplication {
    public static void main(String[] args) {
        // false
        //-Dmyname=jinyunliu
        //[--server.port=7777]
        System.out.println(Thread.currentThread().isDaemon());
        System.out.println("-Dmyname=" +System.getProperty("myname"));
        System.out.println(Arrays.toString(args));

        System.out.println("\n\n\n\n\n\n\n\n");
        SpringApplication.run(EntryApplication.class, args);
    }

    private Thread thread;

    @GetMapping("/first")
    public String first() {
        thread = Thread.currentThread();
        System.out.println(thread.getName());
        LockSupport.park();
        return "first thread";
    }

    @GetMapping("/second")
    public String second() {
        LockSupport.unpark(thread);
        return "second thread";
    }
}
