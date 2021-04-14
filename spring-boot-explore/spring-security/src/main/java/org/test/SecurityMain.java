package org.test;

/**
 * 既然是引入的starter包, 首先看下它的自动配置
 *
 * spring 引入的starter
 * 实际是引入了 spring-boot-starter 然后 这个包里面有一个 autoconfigure
 * 所有直接看这个包即可。EnableAutoConfiguration=*
 *
 *
 * org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,\
 * org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration,\
 * org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration,\
 * org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration,\
 * org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration,\
 * org.springframework.boot.autoconfigure.security.rsocket.RSocketSecurityAutoConfiguration,\
 * org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyAutoConfiguration,\
 *
 *
 * org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration,\
 * org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration,\
 * org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration,\
 * org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration,\
 *
 *
 * spring 对 OAuth2 的封装jar 已经全部弃用
 * <a href='https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide'>
 *     看spring如何把SpringSecurityOAuth2集成到SpringSecurity</a>
 *
 *
 * @author: jinyun
 * @date: 2021/4/14
 */
public class SecurityMain {
    //
    //
    //


    /**
     * <h3>Client</h3>
     * <p>Simplified Enablement 简化了启动</p>
     * OAuth2: 需要加一个注解 @EnableOAuth2Client  现在需要实例化并暴露一个 OAuth2ClientContext 的实例对象
     * see {@link org.springframework.cloud.commons.security.ResourceServerTokenRelayAutoConfiguration}
     * org.springframework.security.oauth2.client.OAuth2ClientContext; 暂时没有搜索到这个类(接口) 弃用 这个是SSO的
     *
     * <p>SS 提供了一个客户端 oauth2Client</p>
     *
     *
     */
    public void difference() {}
}
