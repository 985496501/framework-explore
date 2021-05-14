package org.example.rabbitmq.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Defines all queue names shared by both producer and consumer.
 *
 * @author: jinyun
 * @date: 2021/5/13
 */
@Getter
@RequiredArgsConstructor
public enum QueueNameEnum {
    /**
     * 定义延迟队列, 用于延迟消费消息
     */
    DELAY_QUEUE("delayQueue", "延迟队列"),

    /**
     * 定义死信队列, 用于延迟队列消费失败
     * 1. basic.reject()
     * 2. ttl expired
     * 3. exceed the length of queue
     */
    DEAD_QUEUE("deadQueue", "死信队列");


    private final String queueName;
    private final String desc;
}
