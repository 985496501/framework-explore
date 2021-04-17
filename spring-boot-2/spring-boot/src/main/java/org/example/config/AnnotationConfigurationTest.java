package org.example.config;

import org.example.beans.bean.ImportMyBean;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 探索一下 @Configuration;
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * <pre class="code">
 * &#064;Configuration
 * public class AppConfig {
 *
 * }
 * </pre>
 *
 * @author: jinyun
 * @date: 2021/3/26
 */
public class AnnotationConfigurationTest {

    @Test
    public void annotationConfigApplicationContextTest() {
        AnnotationConfigApplicationContext ac
                = new AnnotationConfigApplicationContext(ImportMyBean.class);
        // 把一个定义的class 封装成beanDefinition 注册到 BeanDefinitionRegistry
//        ac.
        ac.register(ImportMyBean.class);
        ac.refresh();
        System.out.println(ac);
    }

}
