package org.example.config;

import org.example.client.RadicalClient;
import org.example.properties.DemoProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Liu Jinyun
 * @date: 2021/2/16/18:41
 */

@Configuration
@EnableConfigurationProperties(DemoProperties.class)
@ConditionalOnProperty(
        prefix = "demo",
        value = "dev",
        havingValue = "true",
        matchIfMissing = true
)
public class DemoConfig {
    private final DemoProperties demoProperties;

    public DemoConfig(DemoProperties demoProperties) {
        this.demoProperties = demoProperties;
    }

    @Bean
    public RadicalClient radicalClient() {
        return new RadicalClient(demoProperties.getUrl(), demoProperties.getMethod());
    }
}
