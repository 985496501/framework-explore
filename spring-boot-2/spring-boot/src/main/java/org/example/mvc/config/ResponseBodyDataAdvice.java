package org.example.mvc.config;

import org.example.mvc.data.ResponseData;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewRequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author: jinyun
 * @date: 2021/5/7
 */
@Component
public class ResponseBodyDataAdvice implements ResponseBodyAdvice, CommandLineRunner, BeanFactoryAware {
    public static final boolean jackson2Present;

    static {
        jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", ResponseBodyDataAdvice.class.getClassLoader()) &&
                ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", ResponseBodyDataAdvice.class.getClassLoader());
    }


    private BeanFactory beanFactory;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        ResponseData<Object> objectResponseData = new ResponseData<>();
        objectResponseData.setCode(HttpStatus.OK.value());
        objectResponseData.setMessage(HttpStatus.OK.getReasonPhrase());
        objectResponseData.setData(body);
        return objectResponseData;
    }

    @Override
    public void run(String... args) {
        RequestMappingHandlerAdapter bean = beanFactory.getBean(RequestMappingHandlerAdapter.class);

        if (jackson2Present) {
            bean.setRequestBodyAdvice(Collections.singletonList(new JsonViewRequestBodyAdvice()));
            bean.setResponseBodyAdvice(Arrays.asList(new JsonViewResponseBodyAdvice(), this));
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
