package org.example.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;

/**
 * @author: jinyun
 * @date: 2021/5/6
 */
@Slf4j
@Component
public class MyWebApplicationInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
      log.error("-------------------> this class implements WebApplicationInitializer, which will be detected automatically by SpringServletContainerInitializer  SPI");
      log.error("-------------------> config the servletContex ....");
    }
}
