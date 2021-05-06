package org.example.beans.factory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author: jinyun
 * @date: 2021/4/30
 */
@Component
public class MannualRegisterSingletons implements BeanFactoryAware {
    private DefaultListableBeanFactory defaultListableBeanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        } else {
            throw new RuntimeException("runtime exeception ...");
        }
    }



}
