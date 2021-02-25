package org.example.contract.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringContextFacade implements ApplicationContextAware {
    private static ApplicationContext context;

    public SpringContextFacade() {}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return clazz == null ? null : context.getBean(clazz);
    }

}
