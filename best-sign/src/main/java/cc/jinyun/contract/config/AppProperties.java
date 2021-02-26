package cc.jinyun.contract.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties("app.developer")
public class AppProperties {
    private String serverHost;
    private String developId;
    private String privateKey;
}
