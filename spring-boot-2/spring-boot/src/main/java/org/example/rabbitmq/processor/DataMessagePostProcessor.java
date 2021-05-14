package org.example.rabbitmq.processor;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * @author: jinyun
 * @date: 2021/5/13
 */
public class DataMessagePostProcessor implements MessagePostProcessor {
    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        System.out.println("messagePostProcessor: " + message);
        return null;
    }
}
