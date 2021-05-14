package org.example.rabbitmq;

import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.SocketConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.example.rabbitmq.constant.RabbitMQConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * {@link org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration}
 *
 *
 * 配置MQ相关
 * <p>
 * RabbitMQ host. Ignored if an address is set.
 * spring.rabbitmq.host=localhost
 * <p>
 * RabbitMQ port. Ignored if an address is set. Default to 5672, or 5671 if SSL is enabled.
 * spring.rabbitmq.port=5672
 * <p>
 * Login user to authentication to the broker.
 * spring.rabbitmq.username=guest
 * spring.rabbitmq.password=guest
 * <p>
 * spring.rabbitmq.ssl=Ssl 用一个对象存储相关ssl   secure socket layer. 在会话层进行ssl握手;
 * <p>
 * Virtual  host to use when connecting to the broker.
 * spring.rabbitmq.virtualHost=/
 * <p>
 * Comma-separated list of addresses to which the client should connect. When set, the host and port are ignored.
 * It's recommended use it.
 * spring.rabbitmq.addresses=localhost:5672
 * <p>
 * spring.rabbitmq.addressShuffleMode=NONE
 * <p>
 * Requested heartbeat timeout; zero for none. If a duration suffix is not specified, seconds will be used.
 * spring.rabbitmq.requestedHeartbeat
 * <p>
 * Number of channels per connection requested by the client. Use 0 for unlimited.
 * spring.rabbitmq.requestedChannelMax=2047
 * <p>
 * Whether to enable publisher returns.
 * spring.rabbitmq.publisherReturns=
 * <p>
 * Type of publisher confirms to use.
 * spring.rabbitmq.publisherConfirmType
 * <p>
 * 所有的连接 都有这个属性  这个值也要设置一下 不然就一直等待 这不浪费资源吗?
 * Connection timeout. Set it zero to wait forever.
 * spring.rabbitmq.connectionTimeout
 * <p>
 * Continuation timeout for RPC calls in channels. Set it to zero to wait forever.
 * spring.rabbitmq.channelRPCTimeout=Duration.ofMinutes(10)
 * <p>
 * spring.rabbitmq.cache=new Cache()
 * <p>
 * spring.rabbitmq.listener
 * <p>
 * spring.rabbitmq.template
 * <p>
 * <p>
 * 通过配置装配 ConnectionFactory
 * Nagle's algorithm: 为了减少小的数据包发送开启的配置, 直到服务端有明确的应答, 或者 tcp缓冲区已经积攒了一定的数据包才会发送;
 * If you'd like to disable the Nagle's algorithm, set TCP_NODELAY = true.  tcp 不延迟就是no_delay.
 * <p>
 * Connection Recovery, automaticRecovery = true.  setNetworkRecoveryInterval(10 * 1000) retry interval.
 * Network connection between clients and RabbitMQ nodes can fail. RabbitMQ java client supports automatic
 * recovery of connections and topology.  [tə'pɒlədʒɪ] 拓扑结构.
 * 客户端和node节点的网路连接 会失败, 客户端支持 自动恢复 连接和拓扑。 就是Queue, Exchange, Binding
 * The automatic recovery process for many applications follows the following steps:
 * 1. Reconnection
 * 2. Restore connection listeners
 * 3. Re-open channels
 * 4. Restore channel listeners
 * 5. Restore channel basic.qos setting, publisher confirms and transaction settings
 * <p>
 * Topology recovery includes the following actions, performed for every channel
 * 1. Re-declare exchanges, except for predefined ones
 * 2. Re-declare queues
 * 3. Recover all bindings
 * 4. Recover all consumers
 * <p>
 * <p>
 * Automatic connection recovery, if enabled, will be triggered by the following events:
 * An I/O exception is thrown in connection's IO loop
 * A socket read operation times out
 * Missed server heartheats are detected
 * Any other unexpected exception is thrown is connection's IO loop.
 * <p>
 * whichever happens first:
 *
 * @author: jinyun
 * @date: 2021/5/10
 */
@Slf4j
@Configuration
public class RabbitMQConfig {

    @Configuration
    @EnableConfigurationProperties(RabbitProperties.class)
    public static class RabbitConnectionFactoryCreator {
        /**
         * For most use cases, the PooledChannelConnectionFactory should be used.
         * This factory manages a single connection and two pools of channels, based on the Apache Pool2.
         * One pool is for transactional channels, the other is for non-transactional channels.
         * The pools are GenericObjectPool s  with default configuration; a callback is provided to
         * configure the pools; refer to the Apache documentation for more information.
         *
         * @param rabbitProperties properties
         * @return connectionFactory
         */
        @Bean
        public PooledChannelConnectionFactory pooledChannelConnectionFactory(RabbitProperties rabbitProperties) {
            PropertyMapper mapper = PropertyMapper.get();

            // Convenience factory class to facilitate opening a Connection to a RabbitMQ node.
            // Most connection and socket settings are configured using this factory.
            ConnectionFactory connectionFactory = new ConnectionFactory();
            mapper.from(rabbitProperties::getHost).to(connectionFactory::setHost);
            mapper.from(rabbitProperties::getPort).whenNonNull().to(connectionFactory::setPort);
            mapper.from(rabbitProperties::getVirtualHost).to(connectionFactory::setVirtualHost);
            // Either username or password modified, it'll instantiate a new CredentialsProvider.
            mapper.from(rabbitProperties::getUsername).to(connectionFactory::setUsername);
            mapper.from(rabbitProperties::getPassword).to(connectionFactory::setPassword);

            // Enable Nagle's algorithm, disable default. Avoid 网络拥塞; a little delay.
            SocketConfigurator enableNagle = socket -> socket.setTcpNoDelay(false);
            connectionFactory.setSocketConfigurator(enableNagle);
            PooledChannelConnectionFactory factory = new PooledChannelConnectionFactory(connectionFactory);
            factory.setConnectionNameStrategy(connectionNameStrategy());
            // ? ...  todo: how this work?
            factory.setPoolConfigurer((pool, tx) -> {
                if (tx) {
                    // configure the transactional pool;
                } else {
                    // configure the non-transactional pool;
                }
            });

            ConnectionFactory rabbitConnectionFactory = factory.getRabbitConnectionFactory();
            log.info("{}", JSONUtil.toJsonPrettyStr(rabbitConnectionFactory));
            return factory;
        }

        @Bean
        public ConnectionNameStrategy connectionNameStrategy() {
//            return new SimplePropertyValueConnectionNameStrategy("spring.application.name");
            return connectionFactory -> "我刘某人的测试连接";
        }
    }


    @Configuration
    public static class QueueConfigurer {
        /**
         * declare a queue named with entry.
         *
         * @return queue.
         */
        @Bean
        public Queue queue() {
            HashMap<String, Object> extensionAttr = new HashMap<>(2);
            extensionAttr.put(QueueAttrExtension.X_DEAD_LETTER_EXCHANGE, RabbitMQConstant.DEFAULT_TOPIC_EXCHANGE);
            extensionAttr.put(QueueAttrExtension.X_DEAD_LETTER_ROUTING_KEY, RabbitMQConstant.DEAD_QUEUE);
            return new Queue(RabbitMQConstant.QUEUE, true, false, false, extensionAttr);
        }

        /**
         * 使用延迟队列有一个非常大的缺点 就是延迟时间不能修改, 这就坏掉了啊
         * <p>
         * Parameter Template:
         *
         * <ol>
         *     <li><strong>name:</strong> the name of the queue - must not be null; (队列的名字一定不能为空, 因为大多数情况下这个队列名字充当 RoutingKey 的作用)</li>
         *     <li>durable: whether or not persistent the message, must be true</li>
         *     <li>exclusive: whether or not exclude others, must be false. The lifecycle of queue is sync with the Connection.
         *          If the connection disconnects to the RabbitMQ broker, the queue here declared will be deleted.
         *     </li>
         *     <li>autoDelete: whether or not automatically delete the message, must be false</li>
         *     <li>arguments: extensive attrs.</li>
         * </ol>
         * see {@link QueueAttrExtension}
         *
         * @return queue named by {@link RabbitMQConstant#DELAY_QUEUE}
         */
        //@Bean
        @Deprecated
        public Queue delayQueue() {
            HashMap<String, Object> extensionAttr = new HashMap<>(4);
            extensionAttr.put(QueueAttrExtension.X_MESSAGE_TTL, 30 * 1000);
            extensionAttr.put(QueueAttrExtension.X_DEAD_LETTER_EXCHANGE, RabbitMQConstant.DEFAULT_TOPIC_EXCHANGE);
            extensionAttr.put(QueueAttrExtension.X_DEAD_LETTER_ROUTING_KEY, RabbitMQConstant.DEAD_QUEUE);
            return new Queue(RabbitMQConstant.DELAY_QUEUE, true, false, false, extensionAttr);
        }

        /**
         * 声明一个死信队列
         *
         * @return dead queue
         */
        @Bean
        public Queue deadQueue() {
            return new Queue(RabbitMQConstant.DEAD_QUEUE);
        }

        /**
         * bind queue to a directExchange named with amq.direct
         *
         * @return binding
         */
        @Bean
        public Binding binding() {
            return BindingBuilder.bind(queue()).to(new DirectExchange(RabbitMQConstant.DIRECT_EXCHANGE))
                    .withQueueName();
        }

        //@Bean
        public Binding delayQueueBinding() {
            return BindingBuilder.bind(delayQueue()).to(new DirectExchange(RabbitMQConstant.DIRECT_EXCHANGE))
                    .withQueueName();
        }

        @Bean
        public Binding deadQueueBinding() {
            return BindingBuilder.bind(deadQueue()).to(new TopicExchange(RabbitMQConstant.DEFAULT_TOPIC_EXCHANGE))
                    .with(RabbitMQConstant.DEAD_QUEUE);
        }


        /**
         * arguments：队列的其他属性参数，有如下可选项，可参看图2的arguments：
         * （1）x-message-ttl：消息的过期时间，单位：毫秒；
         * （2）x-expires：队列过期时间，队列在多长时间未被访问将被删除，单位：毫秒；
         * （3）x-max-length：队列最大长度，超过该最大值，则将从队列头部开始删除消息；
         * （4）x-max-length-bytes：队列消息内容占用最大空间，受限于内存大小，超过该阈值则从队列头部开始删除消息；
         * （5）x-overflow：设置队列溢出行为。这决定了当达到队列的最大长度时消息会发生什么。有效值是drop-head、reject-publish或reject-publish-dlx。仲裁队列类型仅支持drop-head；
         * （6）x-dead-letter-exchange：死信交换器名称，过期或被删除（因队列长度超长或因空间超出阈值）的消息可指定发送到该交换器中；
         * （7）x-dead-letter-routing-key：死信消息路由键，在消息发送到死信交换器时会使用该路由键，如果不设置，则使用消息的原来的路由键值
         * （8）x-single-active-consumer：表示队列是否是单一活动消费者，true时，注册的消费组内只有一个消费者消费消息，其他被忽略，false时消息循环分发给所有消费者(默认false)
         * （9）x-max-priority：队列要支持的最大优先级数;如果未设置，队列将不支持消息优先级；
         * （10）x-queue-mode（Lazy mode）：将队列设置为延迟模式，在磁盘上保留尽可能多的消息，以减少RAM的使用;如果未设置，队列将保留内存缓存以尽可能快地传递消息；
         * （11）x-queue-master-locator：在集群模式下设置镜像队列的主节点信息。
         */
        interface QueueAttrExtension {
            /**
             * 消息的过期时间 单位： 毫秒
             */
            String X_MESSAGE_TTL = "x-message-ttl";

            /**
             * 死信交换器名称，过期或被删除（因队列长度超长或因空间超出阈值）的消息可指定发送到该交换器中
             */
            String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";

            /**
             * 死信消息路由键，在消息发送到死信交换器时会使用该路由键，如果不设置，则使用消息的原来的路由键值
             */
            String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
        }
    }


}
