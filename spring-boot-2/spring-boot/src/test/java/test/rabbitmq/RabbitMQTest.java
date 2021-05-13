package test.rabbitmq;

import lombok.AllArgsConstructor;
import org.example.EntryApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;

/**
 *
 * @author: jinyun
 * @date: 2021/4/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EntryApplication.class)
public class RabbitMQTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void sendMessageTest() {
        // convert a java object to an Amqp message and send it to a default exchange with a specific routing key;
        rabbitTemplate.convertAndSend("entry", new Data("hello world"));
    }



    @Test
    public void amqpSendTest() {
        amqpTemplate.convertAndSend("entry", new Data("hello world"));
    }


    @AllArgsConstructor
    static class Data implements Serializable {
        private final String name;
    }
}
