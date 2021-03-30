package org.example;

import cc.jinyun.contract.pojo.callback.BestSignNotifyResult;
import cn.hutool.json.JSONUtil;
import org.example.trans.CustomHandler;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.concurrent.locks.LockSupport;

/**
 * SpringBoot Entry.
 *
 * @author: jinyun
 * @date: 2021/2/8
 */
//https://www.cnblogs.com/xuwenjin/p/13141132.html
@EnableWebSocket
@RestController
@SpringBootApplication
@EnableAsync
@EnableScheduling
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

    @Resource
    CustomHandler customHandler;

    /**
     * {
     *     "params": {
     *         "sid": "0",
     *         "account": "17862328960"
     *     },
     *     "action": "signCatalogContract"
     * }
     *
     * @param bestSignNotifyResult
     * @param httpServletRequest
     */
    @PostMapping("callback")
    public void callback(@RequestBody BestSignNotifyResult bestSignNotifyResult, HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest.getHeader("rtick"));
        System.out.println(httpServletRequest.getHeader("sign"));
        System.out.println(JSONUtil.toJsonPrettyStr(bestSignNotifyResult));
    }

    @Test
    public void scanTest() {
        AnnotationMetadata annotationMetadata = AnnotationMetadata.introspect(EntryApplication.class);
        annotationMetadata.getAnnotationTypes().forEach(System.out::println);
        System.out.println(annotationMetadata.hasAnnotation("org.springframework.boot.autoconfigure.SpringBootApplication"));
    }
}
