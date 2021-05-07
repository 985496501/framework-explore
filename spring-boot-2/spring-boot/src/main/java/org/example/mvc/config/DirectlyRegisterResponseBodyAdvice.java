package org.example.mvc.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author: jinyun
 * @date: 2021/5/7
 */

public class DirectlyRegisterResponseBodyAdvice implements BeanFactoryAware {
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }
}
