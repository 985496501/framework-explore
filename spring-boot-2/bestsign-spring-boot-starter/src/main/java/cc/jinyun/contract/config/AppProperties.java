package cc.jinyun.contract.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties("bestsign.developer")
public class AppProperties {
    /**
     * 请求主机域名
     */
    private String serverHost;
    /**
     * 开发id
     */
    private String developId;
    /**
     * 密钥
     */
    private String privateKey;
    /**
     * 合同签署的回调地址
     */
    private String pushUrl;
    /**
     * 公司账户
     */
    private String companyAccount;
    /**
     * 公司名称
     */
    private String companyName;
}
