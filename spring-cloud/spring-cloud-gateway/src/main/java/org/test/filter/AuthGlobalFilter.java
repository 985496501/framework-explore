package org.test.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义一个全局过滤器  用于简单的认证
 *
 * @author: jinyun
 * @date: 2021/4/15
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    public AuthGlobalFilter() {
    }

    /**
     * 认证
     *
     * @param exchange 封装了req-resp
     * @param chain    全局过滤器链表
     * @return void
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return (Ordered.HIGHEST_PRECEDENCE + 8);
    }
}
