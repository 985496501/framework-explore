package org.example.mvc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * 但是并没有自动装配这个类的 onStartup();
 *
 * @author: Liu Jinyun
 * @date: 2021/4/4/0:23
 */
@Component
@Slf4j
public class MyWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("web application initializer ==========================>");
        log.info("进行自定义配置 global servlet-context... {}", servletContext.toString());
    }
}
