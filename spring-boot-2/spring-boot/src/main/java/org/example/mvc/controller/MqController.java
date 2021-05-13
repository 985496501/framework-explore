package org.example.mvc.controller;

import org.example.dto.SimpleData;
import org.example.rabbitmq.RabbitMQConstant;
import org.springframework.amqp.core.AmqpTemplate;
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

    @GetMapping("/mq")
    public void mq() {
        amqpTemplate.convertAndSend(RabbitMQConstant.direct_exchange, RabbitMQConstant.queue, new SimpleData("hello world"));
    }
}
