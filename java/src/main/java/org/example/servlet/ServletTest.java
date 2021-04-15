package org.example.servlet;

import io.undertow.servlet.handlers.DefaultServlet;
import org.junit.Test;

import javax.servlet.Servlet;

/**
 * @author: jinyun
 * @date: 2021/3/18
 */
public class ServletTest {
    /**
     * servlet是一种规范，所有的servlet都必须实现这个接口的所有方法
     *
     */
    @Test
    public void servletTest() {
        Servlet servlet = new DefaultServlet();

    }
}
