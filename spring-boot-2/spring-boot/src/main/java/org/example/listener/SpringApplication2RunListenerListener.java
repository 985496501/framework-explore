package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author: jinyun
 * @date: 2021/3/3
 */
@Slf4j
public class SpringApplication2RunListenerListener implements SpringApplicationRunListener {
    private final SpringApplication application;
    private final String[] args;

    public SpringApplication2RunListenerListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        log.info("starting(ConfigurableBootstrapContext bootstrapContext)");
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        log.info("environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) ");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("contextPrepared(ConfigurableApplicationContext context) ");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("contextLoaded(ConfigurableApplicationContext context)  ");
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.info("started(ConfigurableApplicationContext context)");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.info("running(ConfigurableApplicationContext context) ");
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("failed(ConfigurableApplicationContext context, Throwable exception) ");
    }
}
