package org.example.cache.redisson;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 这个是redisson这个框架定义的 自定义器 可以自主完成完成 redis 的 config;
 *
 * @author: jinyun
 * @date: 2021/4/21
 */
@Slf4j
@Component
public class RedissonConfigCustomizer implements RedissonAutoConfigurationCustomizer {
    /**
     * 定制redisson的config
     *
     * @param config
     */
    @Override
    public void customize(Config config) {
        // 这里的thread 被所有的redis node客户端共享
        config.setThreads(2);
        config.setNettyThreads(4);

        // 配置singleServer
        Method getSingleServerConfig = ReflectionUtils.findMethod(Config.class, "getSingleServerConfig");
        getSingleServerConfig.setAccessible(true);
        SingleServerConfig singleServerConfig = (SingleServerConfig)ReflectionUtils.invokeMethod(getSingleServerConfig, config);
        singleServerConfig.setConnectTimeout(1000);
        singleServerConfig.setTimeout(2000);
        singleServerConfig.setRetryAttempts(1);

        // 这里面有所有的配置 网络通信相关的, 回去加紧学习网络通络和操作系统相关的知识点
        log.info("single server config: \n {}", JSONUtil.toJsonPrettyStr(singleServerConfig));
    }
}
