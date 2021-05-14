package org.example.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import org.example.dto.SimpleData;
import org.example.rabbitmq.constant.RabbitMQConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: jinyun
 * @date: 2021/5/11
 */
@Component
public class MessageConsumer {

    @RabbitListener(queues = RabbitMQConstant.QUEUE)
    public void entryQueueConsumer(Channel channel, Message message, SimpleData simpleData) {
        try {
            System.out.println(simpleData);
//            channel.basicAck();
            System.out.println(message);
            System.out.println(channel);
//            channel.basicAck();
        } catch (Exception e) {
//            channel.basicConsume()
        }
    }

    @RabbitHandler
    public void consumeDelayMessage(Message message, Channel channel) {
        MessageProperties messageProperties = message.getMessageProperties();
    }


}
