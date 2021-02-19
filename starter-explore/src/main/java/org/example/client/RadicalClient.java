package org.example.client;

/**
 * 客户端
 * 手写一个客户端
 *
 * 现在这个客户端的功能大概有：
 * 高性能, 吞吐量高, 低延迟, 线程模型 和 内存模型一定优越.
 *
 * 客户端          服务内     调用第三方
 * 1 负载均衡       +           -
 * 2 容错          +            -
 * 3 重试          +              +
 * 4 降级          +            -
 *
 *
 * 如果第三方封装了 sdk client, 可以酌情考虑是否使用
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/2/16/19:05
 */
public class RadicalClient {
    // 使用开源的 http 客户端 OKHttp客户端
    // 首先了解这个客户端的性能 优势在哪?
    // 看看hystrix是如何解决重试机制的?

    // 现在的客户端访问
    // 1. 一个地址, 通过参数来进行路由功能
    // 2. 一个URL+path  不同的请求

    private String url;
    private String method;

    public RadicalClient(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public String exchange() {
        String msg = "请求地址:" + this.url + "  请求方法:" + this.method;
        System.out.println(msg);
        return msg;
    }
}
