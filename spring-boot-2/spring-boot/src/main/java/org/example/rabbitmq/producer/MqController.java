package org.example.rabbitmq.producer;

import org.example.dto.SimpleData;
import org.example.rabbitmq.constant.RabbitMQConstant;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: jinyun
 * @date: 2021/5/11
 */
@RestController
public class MqController {
    @Autowired
    private AmqpTemplate amqpTemplate;


    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @GetMapping("/mq")
    public void mq() {
//        amqpTemplate.convertAndSend(RabbitMQConstant.DIRECT_EXCHANGE, RabbitMQConstant.QUEUE, new SimpleData("hello world"),
//                new DataMessagePostProcessor());
        amqpTemplate.convertAndSend(RabbitMQConstant.DIRECT_EXCHANGE, RabbitMQConstant.QUEUE, new SimpleData("hello world"));
    }

    @GetMapping("/delay")
    public void delay() {
        amqpTemplate.convertAndSend(RabbitMQConstant.DIRECT_EXCHANGE, RabbitMQConstant.DELAY_QUEUE, new SimpleData("delay task(延迟任务)"));
    }

    @GetMapping("mq2")
    public void mq2() {
//        rabbitMessagingTemplate.convertSendAndReceive()
    }
}
