package org.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: jinyun
 * @date: 2021/4/19
 */
@Configuration
public class TestConfiguration {

    @Bean
    public A a() {
        return new A();
    }
}
