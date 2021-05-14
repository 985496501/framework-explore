package org.example.seckill.dispatcher;

import org.example.seckill.SeckillPipeline;

/**
 * 定义一个管道选择器
 *
 * @author: jinyun
 * @date: 2021/5/14
 */
public interface SeckillDispatcher {
    SeckillPipeline choose();
}
