package org.example.mvc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Liu Jinyun
 * @date: 2021/4/4/21:48
 */
@RestController
public class TestController {

    private final AtomicInteger requestCount = new AtomicInteger(0);

    @GetMapping("getBean")
    public Map<String, String> getBean(Integer id) {
        Map<String, String> mp =  new HashMap<>(2);
        mp.put("name", "org.example.mvc.controller.TestController.getBean");
        mp.put("test", requestCount.incrementAndGet() + " 次");
        return mp;
    }
}
