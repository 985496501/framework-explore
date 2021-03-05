package org.example.listener;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author: jinyun
 * @date: 2021/3/3
 */
public class CustomListener implements SpringApplicationRunListener {

    public CustomListener(SpringApplication application, String[] args) {
        System.out.println("我这里可以配置一下SpringApplication application 吧");
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        System.out.println("starting(ConfigurableBootstrapContext bootstrapContext)");
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        System.out.println("environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) ");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("contextPrepared(ConfigurableApplicationContext context) ");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("contextLoaded(ConfigurableApplicationContext context)  ");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        System.out.println("started(ConfigurableApplicationContext context)");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        System.out.println("running(ConfigurableApplicationContext context) ");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("failed(ConfigurableApplicationContext context, Throwable exception) ");
    }
}
