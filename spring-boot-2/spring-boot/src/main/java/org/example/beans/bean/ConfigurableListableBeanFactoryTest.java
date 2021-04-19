package org.example.beans.bean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 *
 * see {@link ConfigurableListableBeanFactory}
 * @author: jinyun
 * @date: 2021/4/19
 */
public class ConfigurableListableBeanFactoryTest {

    @Test
    public void beanFactoryTest() {
        ConfigurableListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 这个配置的实现 ==> 忽略依赖的接口
        beanFactory.ignoreDependencyInterface(null);
        // 注册 可解析 的依赖;
        beanFactory.registerResolvableDependency(null, null);
    }
}
