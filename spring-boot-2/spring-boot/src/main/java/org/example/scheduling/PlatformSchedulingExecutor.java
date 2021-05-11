package org.example.scheduling;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 下面这个注解就是通过 BeanPostProcessor 处理的;
 *
 * 但是在分布式环境下 将任务调度 和  异步都提取到了 相应的中间件中
 * 比如 xxl-job  rabbitMQ
 *
 * 分布式统一定义任务： 集中管理
 * 1. 在单体架构下 定时任务 必须在启动之前声明 并且一旦启动  就不允许动态修改; xxl-job可以动态执行, 可以修改, 就是一个触发器 trigger
 * 2. 没有统计, 没有信息采集的中控平台; xxl-job 可以提供详细的面板, 可以提供各种日志
 *
 *
 * @author: jinyun
 * @date: 2021/5/10
 */
//@Configuration
//@EnableScheduling
public class PlatformSchedulingExecutor implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }

    @Bean
    @ConditionalOnMissingBean(TaskScheduler.class)
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    /**
     * spring单体封装的 定时任务, 轮训任务执行
     */
    @Scheduled(fixedRate = 10000)
    public void scheduledAtFixedRate() {
        System.out.println("hello  ultimate=final=basic,  synthetic");
    }

    /**
     * 每天两点执行一次, 定点执行
     */
    @Scheduled(cron = "0 0 14 * * ?")
    public void executeAtFixedDatetime() {
        System.out.println("每天两点执行一次");
    }
}
