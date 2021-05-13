package org.example.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import org.example.dto.SimpleData;
import org.example.rabbitmq.RabbitMQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: jinyun
 * @date: 2021/5/11
 */
@Component
public class MessageConsumer {

    @RabbitListener(queues = RabbitMQConstant.queue)
    public void entryQueueConsumer(SimpleData simpleData, Channel channel) {
        try {
            System.out.println(simpleData);
//            channel.basicAck();
        } catch (Exception e) {

        }
    }
}
