package cc.jinyun.contract.config;

import cc.jinyun.contract.api.ContractApi;
import cc.jinyun.contract.client.HttpClient;
import cc.jinyun.contract.api.impl.ContractApiImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppEnvConfig {
    private final AppProperties properties;

    public AppEnvConfig(AppProperties properties) {
        this.properties = properties;
    }

    @Bean
    public HttpClient httpClient() {
        return new HttpClient(properties.getDevelopId(), properties.getPrivateKey(), properties.getServerHost());
    }

    @Bean
    public ContractApi contractApi(HttpClient httpClient) {
        // 使用代理对象
        return new ContractApiImpl(httpClient);
    }
}
