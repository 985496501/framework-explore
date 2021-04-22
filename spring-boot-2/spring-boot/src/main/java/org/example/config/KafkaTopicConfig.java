package org.example.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  看autoconfig这个包, 这个包 有spring所有的配置;
 *  see {@link KafkaAutoConfiguration}
 *
 *  ###########【Kafka集群】###########
 * spring.kafka.bootstrap-servers=112.126.74.249:9092,112.126.74.249:9093
 * ###########【初始化生产者配置】###########
 * # 重试次数
 * spring.kafka.producer.retries=0
 * # 应答级别:多少个分区副本备份完成时向生产者发送ack确认(可选0、1、all/-1)
 * spring.kafka.producer.acks=1
 * # 批量大小
 * spring.kafka.producer.batch-size=16384
 * # 提交延时
 * spring.kafka.producer.properties.linger.ms=0
 * # 当生产端积累的消息达到batch-size或接收到消息linger.ms后,生产者就会将消息提交给kafka
 * # linger.ms为0表示每接收到一条消息就提交给kafka,这时候batch-size其实就没用了
 * ​
 * # 生产端缓冲区大小
 * spring.kafka.producer.buffer-memory = 33554432
 * # Kafka提供的序列化和反序列化类
 * spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
 * spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
 * # 自定义分区器
 * # spring.kafka.producer.properties.partitioner.class=com.felix.kafka.producer.CustomizePartitioner
 * ​
 * ###########【初始化消费者配置】###########
 * # 默认的消费组ID
 * spring.kafka.consumer.properties.group.id=defaultConsumerGroup
 * # 是否自动提交offset
 * spring.kafka.consumer.enable-auto-commit=true
 * # 提交offset延时(接收到消息后多久提交offset)
 * spring.kafka.consumer.auto.commit.interval.ms=1000
 * # 当kafka中没有初始offset或offset超出范围时将自动重置offset
 * # earliest:重置为分区中最小的offset;
 * # latest:重置为分区中最新的offset(消费分区中新产生的数据);
 * # none:只要有一个分区不存在已提交的offset,就抛出异常;
 * spring.kafka.consumer.auto-offset-reset=latest
 * # 消费会话超时时间(超过这个时间consumer没有发送心跳,就会触发rebalance操作)
 * spring.kafka.consumer.properties.session.timeout.ms=120000
 * # 消费请求超时时间
 * spring.kafka.consumer.properties.request.timeout.ms=180000
 * # Kafka提供的序列化和反序列化类
 * spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
 * spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
 * # 消费端监听的topic不存在时，项目启动会报错(关掉)
 * spring.kafka.listener.missing-topics-fatal=false
 * # 设置批量消费
 * # spring.kafka.listener.type=batch
 * # 批量消费每次最多消费多少条消息
 * # spring.kafka.consumer.max-poll-records=50
 *
 * @author: jinyun
 * @date: 2021/4/22
 */
@Configuration(proxyBeanMethods = false)
public class KafkaTopicConfig {
    public static final String TEST_TOPIC = "testtopic";

    public static final String PLAIN_JSON_STR = "plainJsonStr";

    @Bean
    public NewTopic newTopic() {
        return new NewTopic(PLAIN_JSON_STR, 2, (short) 1);
    }


}
