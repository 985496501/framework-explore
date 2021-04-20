package org.example.beans.postprocessor;

import org.example.beans.bean.ImportMyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import java.util.function.Supplier;

/**
 * @author: jinyun
 * @date: 2021/4/20
 */
@Import(ImportMyBean.class)
@Order(100)
@Configuration
public class Configuraer {

    @Bean
    public Supplier<String> supplier() {
        return () -> "hello world";
    }
}
