package org.example.rabbitmq.constant;

/**
 * @author: jinyun
 * @date: 2021/5/11
 */
public interface RabbitMQConstant {
    String DIRECT_EXCHANGE = "amq.direct";

    String DEFAULT_TOPIC_EXCHANGE = "amq.topic";

    String QUEUE = "entry";

    String DELAY_QUEUE = "delayQueue";

    String DEAD_QUEUE = "deadQueue";


}
