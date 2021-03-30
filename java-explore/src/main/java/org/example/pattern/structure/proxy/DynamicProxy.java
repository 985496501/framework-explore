package org.example.pattern.structure.proxy;

import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * 软件设计主要就是动态代理 来创建对象
 *
 * @author: jinyun
 * @date: 2021/3/15
 */
public class DynamicProxy {

    // ---------基本的注解------------
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Get {
        String value();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Post {
        String value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Service {
        String value();
    }

    // --------- 行为定义 --------------------


    @Service("test")
    public interface Api {
        @Get("/get")
        String get(Integer id);

        @Post("/post")
        String post(String password);
    }


    static class Client {
        void invoke(String str) {
            System.out.println(str);
        }
    }

    // ----------- 动态代理 -------------------

    @Test
    public void proxyTest() {
        Delegate delegate = new Delegate();
        Api proxy = delegate.proxy(Api.class);
        proxy.get(1);
        proxy.post("sdfasdf");
    }

    @Test
    public void annotationTest() {
        Class<Api> apiClz = Api.class;
        // 通过 [接口类字节码] 获取所有的 [成员方法]
        Method[] declaredMethods = apiClz.getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++) {
            Method declaredMethod = declaredMethods[i];
            System.out.println("方法名称： " + declaredMethod.getName());
            // 获取方法的所有注解循环找自己的注解 然后找对应的注解上面的标识字段
            String path = "";
            Annotation[] declaredAnnotations = declaredMethod.getDeclaredAnnotations();
            for (int i1 = 0; i1 < declaredAnnotations.length; i1++) {
                Annotation declaredAnnotation = declaredAnnotations[i1];
                if (declaredAnnotation instanceof Get) {
                    Get declaredAnnotation1 = (Get) declaredAnnotation;
                    path = declaredAnnotation1.value();
                } else if (declaredAnnotation instanceof Post){
                    Post declaredAnnotation1 = (Post) declaredAnnotation;
                    path = declaredAnnotation1.value();
                }

                // interface org.example.pattern.structure.proxy.DynamicProxy$Post
                // interface org.example.pattern.structure.proxy.DynamicProxy$Get
                Class<? extends Annotation> aClass = declaredAnnotation.annotationType();
                System.out.println("获取的方法上面的注解类型： " + declaredAnnotation.annotationType());
            }

            System.out.println("方面上面注解获取的值: " + path);
            int parameterCount = declaredMethod.getParameterCount();
            System.out.println("方法的参数： " + parameterCount);
            Type[] genericParameterTypes = declaredMethod.getGenericParameterTypes();
            for (int i1 = 0; i1 < genericParameterTypes.length; i1++) {
                System.out.println(genericParameterTypes[i1]);
            }

            // Class implements Type
            Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            for (int i1 = 0; i1 < parameterTypes.length; i1++) {
                System.out.println(parameterTypes[i1].getTypeName());
            }

        }
    }


    static class Delegate {
        private final Client client = new Client();

        public <T> T proxy(Class<T> clazz) {
            Service serviceAlias = clazz.getAnnotation(Service.class);
            StringBuilder sb = new StringBuilder(serviceAlias.value()).append("/");

//            public final class Proxy extends java.lang.reflect.Proxy implements .Proxy0
            return  (T)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {clazz}, new Handler());
        }
    }

    static class Handler implements InvocationHandler{
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Annotation[] annotations = method.getAnnotations();
            String methodPath = "";
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (int i = 0; i < declaredAnnotations.length; i++) {
                Annotation declaredAnnotation = declaredAnnotations[i];
                if (declaredAnnotation instanceof Get) {
                    Get declaredAnnotation1 = (Get) declaredAnnotation;
                    methodPath = declaredAnnotation1.value();
                } else if (declaredAnnotation instanceof Post){
                    Post declaredAnnotation1 = (Post) declaredAnnotation;
                    methodPath = declaredAnnotation1.value();
                }
            }

            // 通过参数个数和类型创建数组
            List<Object> objects = Arrays.asList(args);
            return methodPath;
        }
    }



}
