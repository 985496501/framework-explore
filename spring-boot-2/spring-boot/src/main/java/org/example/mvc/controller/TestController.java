package org.example.mvc.controller;

import org.example.mvc.constant.CustomHeader;
import org.example.mvc.data.RequestData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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
//    @ResponseStatus(value = HttpStatus.OK, reason = "哈拉少")
    public Map<String, String> getBean(Integer id) {
        Map<String, String> mp =  new HashMap<>(2);
        mp.put("name", "org.example.mvc.controller.TestController.getBean");
        mp.put("test", requestCount.incrementAndGet() + " 次");

        // todo: 如何获取当前的的请求对象和返回对象呢？ Spring MVC 的相关实现呢？
//        RequestContextHolder 这个存在当前线程的 RequestAttribute
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        // 两个都一样啊;
        response.setHeader(CustomHeader.sessionId, "setHeader");
        response.addHeader(CustomHeader.dateTime, "addHeader");
        return mp;
    }

    @GetMapping("empty")
    public void empty() {
        System.out.println("empty");
    }


    @GetMapping("getStr")
    public String getStr() {
        return "hello world";
    }

    @GetMapping("getEpt")
    public Exception getException() {
        return new NullPointerException("random exception ...");
    }


    @PostMapping("list")
    public void getList(@RequestBody List<RequestData> list) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Enumeration<String> headers = request.getHeaders("X-Lightchain-Track");

        while (headers.hasMoreElements()) {

        }

        String header = request.getHeader("X-Lightchain-Track");
        System.out.println(header);

        list.forEach(System.out::println);
    }
}
