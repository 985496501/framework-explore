package org.example.spring.listener;

import org.junit.Test;

/**
 * spring application 的创建, 具体的几个阶段, 每个阶段需要另外做的事情
 * 都是通过 Listener 监听器来监听 每个阶段, 当spring走完或者要开始走的时候,
 * 就会触发这个事件.
 *
 * 启动阶段, bootstrap 引导启动类完成, 这个是configurable
 * starting()
 * environmentPrepared()
 *
 * 启动结束, 创建 applicationContext, 完成创建之后进行准备之后的工作
 * 或者 上下文中一些其他的 装载
 * contextPrepared()
 * contextLoaded()
 *
 * 已经启动起来
 * started()
 *
 * 正在运行
 * running()
 *
 * 启动失败
 * failed()
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/2/17/23:01
 */
public class ListenerTest {

    @Test
    public void springApplicationRunListenerTest() {

    }
}
