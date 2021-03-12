package com.example.okhttp.interceptor;

import okhttp3.*;

import java.io.IOException;

/**
 * @author: jinyun
 * @date: 2021/3/12
 */
public class SimpleInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Connection connection = chain.connection();
        System.out.println(connection);

        Request request = chain.request();
        System.out.println(request);

        // continue == proceed.
        // The instance of Headers is immutable, never be changed. Use builder to modify.
        Headers headers = request.headers();
        System.out.println(headers);
        // intercept request and response
        // 能不能在这里进行定制链路日志呢 把所有的日志链路都存储 然后根据一次链路进行分析 仅仅在开发测试环境使用 为了解决线上问题debug的难点。
        // 同时扩展这个
        return chain.proceed(request);
    }
}
