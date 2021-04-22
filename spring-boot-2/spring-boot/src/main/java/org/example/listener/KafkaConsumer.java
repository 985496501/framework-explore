package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.config.KafkaTopicConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author: jinyun
 * @date: 2021/4/22
 */
@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = {KafkaTopicConfig.TEST_TOPIC})
    public void onMessage(ConsumerRecord<?, ?> record) {
        log.info("-----------------------> 简单消费： topic: {}, partition: {}, value: {}",
                record.topic(), record.partition(), record.value());
    }

    @KafkaListener(topics = {KafkaTopicConfig.PLAIN_JSON_STR})
    public void onMessage(String data) {
        log.info("-----------------------> 简单消费： {}", data);
    }
}
