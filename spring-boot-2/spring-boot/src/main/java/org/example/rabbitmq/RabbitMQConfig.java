package org.example.rabbitmq;

import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.SocketConfigurator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.PooledChannelConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
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

    /**
     * declare a queue named with entry.
     *
     * @return queue.
     */
    @Bean
    public Queue queue() {
        return new Queue(RabbitMQConstant.queue);
    }

    /**
     * bind queue to a directExchange named with amq.direct
     *
     * @return binding
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(new DirectExchange(RabbitMQConstant.direct_exchange)).withQueueName();
    }
}
