package org.example.mvc.config;

import org.example.mvc.data.ResponseData;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 妈的  我实现怎么都不行  看了网上的教程 使用这个注解就可以 {@link ControllerAdvice}
 *
 * @author: jinyun
 * @date: 2021/5/7
 */
@RestControllerAdvice
public class ResponseBodyDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * @param returnType    返回值的类型;
     * @param converterType 转换器的类型;
     * @return
     */
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


    @ExceptionHandler(ClassCastException.class)
    public Object classCastExceptionHandler(ClassCastException e) {
        ResponseData<Object> objectResponseData = new ResponseData<>();
        objectResponseData.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        objectResponseData.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        objectResponseData.setData(e.getMessage());
        return objectResponseData;
    }


}
