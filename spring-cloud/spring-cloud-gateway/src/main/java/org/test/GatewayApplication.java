package org.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.config.GatewayClassPathWarningAutoConfiguration;

/**
 * 直接屏蔽掉这个检查浪费启动速度 和 内存
 *
 * @author: jinyun
 * @date: 2021/4/13
 */
@SpringBootApplication(exclude = GatewayClassPathWarningAutoConfiguration.class)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
