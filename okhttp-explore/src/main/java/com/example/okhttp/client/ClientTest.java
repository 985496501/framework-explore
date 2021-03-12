package com.example.okhttp.client;

import cn.hutool.json.JSONUtil;
import com.example.okhttp.interceptor.SimpleInterceptor;
import okhttp3.*;
import org.example.common.pojo.Chat;
import org.junit.Test;

import java.io.IOException;

/**
 * Accept the request and produce the response.
 * This is simple in theory but it gets tricky in practice.
 *
 *
 * @author: jinyun
 * @date: 2021/3/12
 */
public class ClientTest {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    /**
     * A single OkHttpClient instance and reuse it for all of your calls.
     */
    private final OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * new HttpLogInterceptor 打印log
     */
    private OkHttpClient customizedOkHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new SimpleInterceptor())
            .cache(null)
            .build();


    @Test
    public void clientBuilderTest() {
        // 重新创建一个新的client, 不建议使用
        customizedOkHttpClient = customizedOkHttpClient.newBuilder().build();
        Request request = new Request.Builder().url("http://localhost:7777/second").build();
        try (Response response = customizedOkHttpClient.newCall(request).execute()) {
            System.out.println(response.body().string());
        } catch (IOException e) {
            // swallow and proceed.
        }
    }

    @Test
    public void postTest() {

        RequestBody requestBody = RequestBody.create(JSON, JSONUtil.toJsonStr(new Chat("hello world, i'm an okHttp client")));
        Request request = new Request.Builder().url("http://localhost:7777/postTest").post(requestBody).build();
        try (Response response = customizedOkHttpClient.newCall(request).execute()) {
            System.out.println(response.body().string());
        } catch (IOException e) {

        }

    }

}
