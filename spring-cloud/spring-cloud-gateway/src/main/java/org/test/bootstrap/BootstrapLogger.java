package org.test.bootstrap;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 主要在启动的时候打印一些参数
 *
 * @author: jinyun
 * @date: 2021/4/15
 */
//@Profile("dev")
@Component
public class BootstrapLogger implements EnvironmentAware, SmartInitializingSingleton, Ordered {
    private static final String TEMPLATE = "---application：{} --- port：{} --- environment：{} --- VM options: {}";

    private ConfigurableEnvironment configurableEnvironment;


    @Override
    public void setEnvironment(Environment environment) {
        this.configurableEnvironment = (ConfigurableEnvironment)environment;
    }

    @Override
    public void afterSingletonsInstantiated() {
        String[] activeProfiles = configurableEnvironment.getActiveProfiles();
//        environment.getProperty("-Xms")
//
//        log.info(TEMPLATE, );
        System.out.println("");
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
