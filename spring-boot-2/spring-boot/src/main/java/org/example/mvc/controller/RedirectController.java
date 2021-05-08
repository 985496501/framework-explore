package org.example.mvc.controller;

import org.example.mvc.constant.CustomHeader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: jinyun
 * @date: 2021/5/8
 */
@Controller
public class RedirectController {
    @GetMapping("redirect")
    public void redirect(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setHeader(CustomHeader.sessionId, "156156165123132xxxx");
            response.addHeader(CustomHeader.dateTime, "4165132132132cc");
            response.sendRedirect("/getBean");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
