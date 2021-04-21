package org.example.beans.factory;

import org.example.beans.postprocessor.Configuraer;
import org.example.beans.postprocessor.PostProcessorRegistrationDelegateTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author: jinyun
 * @date: 2021/4/21
 */
public class DefaultListableBeanFactoryTest {

    /**
     * see {@link DefaultListableBeanFactory}
     *
     * see {@link PostProcessorRegistrationDelegateTest#invokeBeanDefinitionRegistryPostProcessorsTest()}
     */
    @Test
    public void beanFactoryTest() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();


//        beanFactory.registerBeanDefinition();
        // getBean 是BeanFactory的方法
        Configuraer bean = beanFactory.getBean(Configuraer.class, null);
        // ==> resolveBean()
        //      resolveNamedBean()
        //      getBeanNamesForType(requiredType)
        //
        assert bean != null;
    }
}
