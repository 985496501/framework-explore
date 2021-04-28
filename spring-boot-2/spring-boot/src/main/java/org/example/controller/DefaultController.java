package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <a href="https://www.zhoulujun.cn/html/tools/NetTools/PacketCapture/7908.html">抓包工具</a>
 *
 * @author: jinyun
 * @date: 2021/4/28
 */
@RestController
public class DefaultController {
    @GetMapping("hello")
    public String hello() {
        return "hello world";
    }
}
