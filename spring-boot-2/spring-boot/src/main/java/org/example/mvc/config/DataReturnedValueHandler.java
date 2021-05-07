package org.example.mvc.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

/**
 * 自定义一个返回值封装对象
 * 将 controller 中的 handlerMethod() returned object  encapsulated.
 *
 * see {@link ServletInvocableHandlerMethod#invokeAndHandle(org.springframework.web.context.request.ServletWebRequest,
 * org.springframework.web.method.support.ModelAndViewContainer, java.lang.Object...)}
 *
 * 虽然我们通过一个集合 把这个塞进去了 但是MVC 只会选择 一个 HandlerMethodReturnedValueHandler ;
 * see {@link org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor}
 *
 * see {@link org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice}
 *
 * @author: jinyun
 * @date: 2021/5/7
 */

public class DataReturnedValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return true;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest){
        System.out.println("hello world");
    }
}
