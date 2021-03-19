package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>Cloud-Native:</p>
 * https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/
 * <p><b>1. Bootstrap Application Context</b></p>
 * <p>A Spring Cloud application operates by creating a 'bootstrap' context, which is <b>a parent context</b> for the main application.
 * This context is responsible for loading configuration properties from the external sources in the local external configuration files.
 * The two contexts share an <b>Environment</b>, which is the source of external properties for any Spring application.
 * Bootstrap.yml properties that are loaded during the bootstrap, with high precedence, so they cannot be overridden by local configuration.</p>
 * <p>
 *     The property sources that are added to your application by the bootstrap context are often from Config Server. By default, they cannot
 *     be overridden locally. The remote property source has to grant it permission by setting spring.cloud.config.allowOverride=true
 * </p>
 *
 * <p><b>2. Cloud Common Abstractions</b></p>
 * <p>
 *      EnableDiscoveryClient: org.springframework.cloud.client.discovery.EnableDiscoveryClient
 *      is no longer required. DiscoveryClient
 * </p>
 *
 *
 *
 * <ol>
 *     <li>service-discovery: 我们使用nacos-discovery来进行服务发现</li>
 *     <li>service-discovery</li>
 *     <li>service-discovery</li>
 *     <li>service-discovery</li>
 *     <li>service-discovery</li>
 *     <li>service-discovery</li>
 *     <li>service-discovery</li>
 * </ol>
 *
 * @author: jinyun
 * @date: 2021/3/16
 */
@SpringBootApplication
//@SpringCloudApplication
public class CloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }
}
