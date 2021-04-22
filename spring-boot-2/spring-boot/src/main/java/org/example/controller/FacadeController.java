package org.example.controller;

import cn.hutool.json.JSONUtil;
import org.example.config.KafkaTopicConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author: jinyun
 * @date: 2021/4/22
 */
@RestController
public class FacadeController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public FacadeController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("kafka/producer")
    public void sendMsg() {
        HashMap<String, String> map = new HashMap<>(2);
        map.put("name", "jinyun");
        kafkaTemplate.send(KafkaTopicConfig.PLAIN_JSON_STR, JSONUtil.toJsonStr(map));
    }
}
