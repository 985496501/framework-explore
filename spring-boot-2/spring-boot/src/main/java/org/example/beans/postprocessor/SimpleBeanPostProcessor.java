package org.example.beans.postprocessor;

import org.example.beans.bean.scan.SimpleBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Deprecated:
 * RequiredAnnotationPostProcessor
 *
 * @author: jinyun
 * @date: 2021/3/16
 */
//@Component
public class SimpleBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("simpleBean")) {
            // This beanName has three forms: plainName, fully qualified name and $Name.
            System.out.println("SimpleBeanPostProcessor ----> post process before initialization....." + beanName);
            SimpleBean simpleBean = (SimpleBean) bean;
            System.out.println("对simpleBean一顿操作猛如虎");
        }

        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
       // System.out.println("SimpleBeanPostProcessor ---->   post process after initialization....." + beanName);
        return null;
    }
}
