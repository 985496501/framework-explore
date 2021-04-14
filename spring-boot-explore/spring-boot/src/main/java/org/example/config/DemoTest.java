package org.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author: jinyun
 * @date: 2021/4/14
 */
@Setter
@Getter
@EnableConfigurationProperties(DemoTest.class)
@ConfigurationProperties("example.config")
public class DemoTest {
    private String demoTest;
}
