package org.example.beans.postprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 这个beanPostProcessor如何初始化的
 *
 * 如何使用特定的beanPostProcessor完成bean的定制呢?
 *
 *
 * 自定义beanPostProcessor 对指定的bean完成后置处理;
 *
 *
 *
 * todo: 以这个为例: 我忘记昨天定制这个 是为啥了? 到底为啥需要扩展 RedissonClient 对象呢?
 *
 *
 * @author: jinyun
 * @date: 2021/4/21
 */
public class RedisTemplateBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
