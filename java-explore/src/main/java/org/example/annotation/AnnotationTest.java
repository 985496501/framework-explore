package org.example.annotation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 注解测试
 * 框架中常常会根据业务 定义一系列注解 然后会跟着 写一套解析注解的系统
 * 来完成功能。
 *
 * 注解 + 接口 + 代理 == 就非常灵活使用了.
 * 通过在接口上加注解, 接口方法上加注解 通过 delegate 完成具体的任务
 * 但是这个都是通过 静态的字节码文件完成的动作 性能上稍微有点欠缺
 * 如果有生命周期维护的容器, 在系统starting的时候, 就要把这些准备的数据
 * 全部初始化 加入到cache中, 这样后面就可以直接使用了。
 *
 * 学习 mybatis  or  feign
 * 然后学习 他们的注解系统
 * spring 的 注解系统
 *
 * https://blog.csdn.net/wangwei249/article/details/55188365
 *
 * @author: jinyun
 * @date: 2021/2/20
 */
public class AnnotationTest {

    @Test
    public void mapTest() {
        Map[] annotationsByType = SampleBean.class.getAnnotationsByType(Map.class);
        for (int i = 0; i < annotationsByType.length; i++) {
            System.out.println(annotationsByType[i].value());
        }

        // 获取某个字段上面的注解
        Field[] declaredFields = SampleBean.class.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            System.out.println(declaredFields[i].getAnnotation(Map.class).value());
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class SampleBean {
        @Map("resourceUrl")
        private String url;
    }


    @Test
    public void getAnnotationsFromClassTest() {
        // Derive annotations from the given object of class that's attached.
        Annotation[] annotations = SampleBean.class.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            System.out.println(annotations[i].annotationType().getCanonicalName());
        }
    }

}
