package org.example.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: Liu Jinyun
 * @date: 2021/2/16/18:36
 */
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {
    private String url;
    private String method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
