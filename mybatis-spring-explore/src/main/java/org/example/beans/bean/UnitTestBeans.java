package org.example.beans.bean;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * --spring.profiles.active=test
 *
 *
 *
 * @author: jinyun
 * @date: 2021/3/16
 */
@Profile("dev")
@Component
public class UnitTestBeans implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("我只在profile=test才会运行我哦哦啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
    }
}
