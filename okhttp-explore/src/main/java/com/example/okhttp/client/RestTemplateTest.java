package com.example.okhttp.client;

import okhttp3.OkHttpClient;
import org.example.common.pojo.Chat;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: jinyun
 * @date: 2021/3/15
 */
public class RestTemplateTest {

    public static final RestTemplate REST_TEMPLATE;
    static {
        final ClientHttpRequestFactory clientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(new OkHttpClient());
        REST_TEMPLATE = new RestTemplate(clientHttpRequestFactory);
        List<HttpMessageConverter<?>> httpMessageConverters = new LinkedList<>();
        httpMessageConverters.add(new StringHttpMessageConverter());
        // ClassNotFoundException

        httpMessageConverters.add(new MappingJackson2HttpMessageConverter());
        REST_TEMPLATE.setMessageConverters(httpMessageConverters);
    }


    /**
     *
     * spring-web encapsulates spring-http core tools.
     */
    @Test
    public void restTemplateTest() {
        String forObject = REST_TEMPLATE.getForObject("http://localhost:7777/second", String.class);
        System.out.println(forObject);
    }

    @Test
    public void postTest() {
        ResponseEntity<String> stringResponseEntity = REST_TEMPLATE.postForEntity("http://localhost:7777/postTest",
                new Chat("hello world, i'm an okHttp client"), String.class);
        HttpHeaders headers = stringResponseEntity.getHeaders();
        headers.forEach((header, headerVals) -> {
            System.out.println(header + ":" + headerVals);
        });
    }
}
