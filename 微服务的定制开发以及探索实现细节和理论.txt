1. CRM: 
1> workflow改造，调整模型的流程节点主要是驳回的上一节点（已经回滚，下班排期再优化 delay）
2> 前端联调支持，bug修复，提供其他遗漏接口
2. App:
1> 一件定制订单的计价规则 测试通过 319上线
2> 大货生产订单的相关规则匹配新的规则
3. framework:
1> okhttp->restTemplate->ribbon->openFeign的组件相关调研工作结束
2> 完成服务调用组件的相关测试和整改


下周任务：
1> 完成famework的 Http 调用链相关 最终改造工作，push到线上分支（openFeign）
2> track CRM 测试结果，及时修复存在的bug



// 2021-3-23
###################################################################################################################################

Spring Cloud:
使用相对最新版本的框架，能够快速进行开发
拥有后台 前端一站式开发脚手架
然后有常用的服务自动已经自动继承， 中台服务。
单体docker
集群的k8s.
还有企业开发常有的组件。
===============================================================================================================================>>
===============================================================================================================================>>
===============================================================================================================================>>
分布式服务 服务配置的解析： 我们使用的是 nacos的配置, 这里看一下客户端的注册流程，服务端的设计先闭包不看。
SpringBoot的 prepareContext() 这个方法发布事件，我们看下这个方法是怎么传递并且调用Ali组件的。
==>prepareContext(AnnotationConfigApplicationContext, StandardEnvironment, EventPublishingRunListener...)
==>applyInitializers(AnnotationConfigApplicationContext)

这里是SpringCloud的服务：
SpringApplication这个应用启动类有一个 属性： initializers, 它里面维护了一个 ApplicationContextInitializer 类型。
==> PropertySourceBootstrapConfiguration. 看下这个类：                  initialize(context)
1>spring.cloud.config=*,  PropertySourceBootstrapProperties这个属性类。 中配置外部属性文件是否可以覆盖系统属性， 默认是可以进行覆盖的。
这个前缀配置是 Cloud 的规范 
org.springframework.cloud.bootstrap.config.PropertySourceLocator 包下的就是启动获取配置的扩展。

2>这个类需要一个beanlist, PropertySourceLocator 这个时候SpringCloud暴露的接口， 需要第三方实现locate(Environment)方法。

==>
这里是SpringCloudAlibaba的具体扩展方法。 
具体就是看我们导入的 spring-cloud-starter-alibaba-nacos-config 这个starter下的jar, 然后里面有 spring.factories.
NacosConfigBootstrapConfiguration:  NacosPropertySourceLocator这个bean是ali实现的spring cloud的规范。

1> 这是第一个配置，它配置了 nacos的外部配置属性 NacosConfigProperties
1.1> NacosConfigProperties: 这个是个配置类： spring.cloud.nacos.config.* 进行配置。  一些常量编译之后自动填充字符串。
这个类配置前缀 spring.cloud.nacos.config=*   这个是 cloudalibaba的配置规范。
默认就是： server-addr=localhost:8848, username=, password=;  其他的具体配置 就找这个类就完事了。
List<Config> sharedConfigs 这个字段配置 spring.cloud.nacos.config.shared-configs[0]=xxx .
这个类里面有一个静态内部类：Config
它配置了 name(dataId), group=DEFAULT_GROUP, refresh=false, prefix(dataId prefix)

1.2> 说明： 它把spring.core.env的Environment 的引用保存到本地的类中 NacosConfigProperties。
我们需要 非常了解这个 环境对象 Environment：
它继承了 PropertyResolver 属性解析器的功能，它具备解析一些属性的能力，特别的一个方法就是：
resolvePlaceHolders(String text), 这个接口是这样规范的，给一个文本字符串比如 ${spring.cloud.nacos.config.server-addr:}
它要求实现 ${spring.cloud.nacos.config.server-addr:} 通过 getProperty("spring.cloud.nacos.config.server-addr:") 如果能获取到这个这个占位符 替换掉。



2> 配置了一个 NacosConfigManager, 这个类就负责调用 底层依赖的 nacos-client.
我们需要简单的了解一下 nacos-common这个包： http.client.*












ConfigService 这个是nacos-api的接口规范，实现就是 nacos-client具体实现。

3> com.alibaba.cloud.nacos.client这个是 spring-cloud-starter-alibaba-nacos-config包下的，具体需要看 NacosPropertySourceLocator 这个类。
ali 实现了 PropertySourceLocator 接口， order(0), 重写了locate(Environment)方法下面主要看下locate()方法的具体做的事情：
3.1> 保存environment
3.2> 创建ConfigService
		看完3.3 之后会看到这个服务的调用， configService.getConfig(dataId, group, timeout);
		NacosConfigService 这个是具体的实现类。 
		ParamUtil.parseNamespace(Properties) 用于解析 namespace 
		getConfig(tenant, dataId, group, timeout), namespace 用于 isolation. seperate environment.

		先理解一下 nacos 定义的 这三个变量的定义：  需要启动nacos的服务端，然后点点自己配置感觉一下这些定义的具体含义：
		namespace: 一般用于区分是什么环境比如 开发 测试  生产， 用于环境的隔离。
		dataId: 
			配置集，data, 在系统中的体现就是一个配置文件就是一个配置集，比如包含了数据源配置，线程池配置，日志配置的一个配置文件就是一个数据集
			数据集id, 就是上面这个配置文件的主键。
			这个是dataId的命名规则：  ${prefix}-${spring.profiles.active}.${file-extension}
			prefix: spring.application.name 默认就是， 也可以通过配置 spring.cloud.nacos.config.prefix=* 进行配置来覆盖默认值
			如果 active 不存在 就会变成 ${prefix}.${file-extension}
			file-extension: spring.cloud.nacos.config.file-extension=* 进行配置，支持yaml, properties两种格式, 默认是properties, 可以配置修改

			/v1/cs/configs 通过这个path调用nacos的服务端的配置数据，  
			dataId: provider, provider.yaml 是两种配置文件 nacosClient都会去拉去这两种配置, 通过下面这个方法：       
			 NacosPropertySourceLocator.loadNacosDataIfPresent()
			第一种是默认的，第二种的优先级比第一种高 会覆盖第一种。

		group: 不同子系统（微服务系统）的配置文件可以放到一个组。 
		我们开发首先一个应用 首先是需要区分 是开发环境还是测试环境还是线上环境， dev, test, prod 一般就是这3个环境。

		我们再细看一个实现类：NacosConfigService 这个类的设计：
		这个类的设计像不像我们写 crud的 service 的实现， 这个类需要借助其他service完成本身的服务，这个依赖的类 HttpAgent, 一个ClientWorker.
		HttpAgent: 这是一个接口，http的代理人， 它的实际对象是 MetricHttpAgent.  ServerHttpAgent
		接口设计：  HttpAgent 是http调用 nacos server的顶级接口
			- ServerHttpAgent 它是一个实现，主要就是它干事情
				++ 我们通过一个threadStack发现 一个线程正在park, 等待别人的唤醒, com.alibaba.nacos.client.Worker.fixed-localhost:8080
			- MetricHttpAgent 它也需要一个干活的人，然后它负责扩展了一些能力。
			- 使用了包装设计模式，这里看看这个设计模式的好处和类图，设计的动机。  
			motivation. --motive: mot ive:n 表示动机  intention,意图，打算，计划， cause n 也是原因动机的意思。 cuz 口语的原因，动机的表述，使得也是这个。

3.3> nacosConfigProperties.timeout 获取配置的超时时间(3000) name: dataId name.
	name, dataIdPrefix=null, 把name赋值给prerfix, 如果还空，获取environment的property("spring.application.name")


4> @Refreshed 注解，这个注解非常强大  可以实现配置的动态加载配置， 后面可以探究一下这个注解的实现逻辑。 目前不用加@Refreshed也可以完成自动刷新


===============================================================================================================================>>
===============================================================================================================================>>
看下nacos的自动完成服务的注册 加入包然后看这个包的starter,  auto-service-registry, 暂时没啥看的啊， 现在 服务发现和调用。
Problem: 如何完成的自动注册服务？ 还是得看一下这个源码：
spring-cloud-starter-alibaba-nacos-discovery的 spring.factories

1. 先看第一个自动配置
@Configuration(
    proxyBeanMethods = false
)
@ConditionalOnDiscoveryEnabled       		# 这个是springcloud规范的完成服务的自动注册
@ConditionalOnNacosDiscoveryEnabled  		# 这个是springcloudAlibaba规范的完成服务的自动注册
NacosDiscoveryAutoConfiguration  nacos服务发现的自动配置
生成了两个bean：
NacosDiscoveryProperties：  服务发现的配置


===============================================================================================================================>>
===============================================================================================================================>>
===============================================================================================================================>>
探究 springboot 解析 bootstrap.yml 配置文件的过程。 具体是怎么完成解析的。 这个在2.4.0版本 这个地方改动很大，先不看这个版本。


















===============================================================================================================================>>
SpringCloud Gateway 的探索
===============================================================================================================================>>
gateway 就是大门， 门户网站，所以需要搞搞这个首先。

引入它的包，然后看官网：
1. security
2. monitoring/metrics
3. resiliency  [rɪˈzɪljənsi]  re silien cy

features:
1. spring5, project reactor, spring webflux.
2. match routes on any request attribute.
3. predicates and filters [routes], easy to write.
4. circuit breaker integration
5. discoveryclient integration
6. rate limiting
7. path rewriting

引入spring-cloud-starter-gateway
它实际依赖了3个包： cloud-starter, server, webflux.
我们首先看自动装配了那些对象。其实我们只能看 spring-cloud-gateway-server.
1. GatewayClassPathWarningAutoConfiguration 这个装配纯属多余就是检查 是不是reactive, classpath检查
可以直接屏蔽掉这个类，没啥用。既可以加快启动速度，还能节约内存。 我草这么屏蔽到底有没有用啊， @AutoConfigureAfter()这个又用到了

2. GatewayAutoConfiguration 这个就是gateway的自动配置 核心配置类了。
@Configuration(proxyBeanMethods = false)   # 老套路了
@ConditionalOnProperty(name = {"spring.cloud.gateway.enabled"},matchIfMissing = true) 默认开启gateway, 这也算是一个暴露的控制开关吧 如果引入了这个starter包不想使用配置这个
@EnableConfigurationProperties
@AutoConfigureBefore({HttpHandlerAutoConfiguration.class, WebFluxAutoConfiguration.class})
@AutoConfigureAfter({GatewayLoadBalancerClientAutoConfiguration.class, GatewayClassPathWarningAutoConfiguration.class})
@ConditionalOnClass({DispatcherHandler.class})

1. 我们先看这个类实例化之前的准备 
GatewayLoadBalancerClientAutoConfiguration: 如果我们没有引入了 loadbalancer 并且引入了 nacos的discovery就会自动配置一个LoadBalancerClientFilter
但是这个已经弃用了，那么我们不用这个。 我们排除调ribbion的包之后就不会走这个 LoadBalancerClientAutoConfig的配置了 相当于没啥用。
org.springframework.web.reactive.DispatcherHandler 这个是 webflux 封装的 dispatcher.

2. 我们看下gateway自动装配的bean
- StringToZonedDateTimeConverter 将string转换成时区时间
- RouteLocatorBuilder 创建一个RouteLocatorBuilder 
- GatewayProperties: pring.cloud.gateway.* 这个是gateway的核心配置类，用于解析外部配置, 存储在内存中。

- Predicate: 先内存方式配置谓词。 我们只保留path方式。

想要学习 http 相关开发
可以看下 spring-web 这个包 学习一下看看它的设计图 如果有新的协议是不是可以仿照着写一手呢，或者其他语言写一个高性能高可扩展的包。 后面在遇到具体情况在仔细看看。
- Filter: 我们再看看这些filter.  有filter肯定有 filterChain;
filter的功能就是对 request 和 response 的 header,body, path 进行修改

我们主要看一个globalFilter, 我们需要通过它对  定制更多业务。 研究一下 sentinel 这个哨兵
用于安全，监控，统计 网关。 首先自定义过滤器，这个就是仿照 Sentinel 的照着写就行。

3. 使用lb:service-name, 配置这个断言 是由全局过滤器完成的操作。 因为这个全局过滤器就会使用 所以我们都要具体看看是什么？
3.1 ForwardRoutingFilter  转发路由过滤器：  先写业务了，后面再看这个， 这个 重定向过滤器 是过滤器中最末尾的。
	重定向： https://www.jianshu.com/p/e56d9f0a5c09
	转发： https://www.jianshu.com/p/aa97810e5fa4?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation

todo: 让本地nacos后台启动：   linux: &,  win: 
standalone:  -Xms256m -Xmx256m -Xmn256m     
https://blog.csdn.net/yrwan95/article/details/82826519  参数 要经常设置这个值

分布式锁的实现原理，如果让你实现一个分布式锁 该如何同步数据。<分布式锁 要通过通信保证 多点内存数据的 一致性>
redis: 妈的只能是 linux 才可以源码编译运行， 看来得 在linux上面玩弄了

看下自动路由的功能点： 以及其他一系列功能点



首先看 spring.factories
然后看这个包下的config pkg 这个包里面有全部的配置扩展点，就是给外界进行配置的;




================================================================================================================================
redis的配置
================================================================================================================================
现实很傻比的通过反射获取timeout, cluster

然后首先的优先级的redisson配置的 config, file 读取config
然后获取redis的配置文件中的sentinel  我们是单体的这个不配置也没有

最后走的是redis 很简单的配置;
















server:
  port: 81
spring: 
  datasource:
    url: jdbc:mysql://192.168.0.119:3306/lightchain?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8
    username: root
    password: zentao123
    driver-class-name: com.mysql.cj.jdbc.Driver
  cloud:
    discovery:
      reactive:
        enabled: false
    gateway:
      discovery:
        locator:
          enabled: true
    loadbalancer:
      retry:
        enabled: true
  

# 聚合文档配置
swagger:
  document:
    resources:
      - name: 授权模块
        location: /light-chain-auth
      - name: 用户管理
        location: /light-chain-customer

xss:
  enable: true
  skip-url:
    - /weixin
    - /payment-callback/weChat
    - /payment-callback/refund/weChat
secure:
  skip-url:
    - /test/**
    - /druid/**

  
































具体看下devTools的强大便利性：



































===============================================================================================================================>>
Spring-Cloud-loadbalancer 的自动配置看看做了些啥
===============================================================================================================================>>
1. LoadBalancerAutoConfiguration. 第一个就是主要的自动装配
@Configuration(proxyBeanMethods = false)
@LoadBalancerClients
@EnableConfigurationProperties(LoadBalancerProperties.class)
@AutoConfigureBefore({ ReactorLoadBalancerClientAutoConfiguration.class,
		LoadBalancerBeanPostProcessorAutoConfiguration.class,
		ReactiveLoadBalancerAutoConfiguration.class })
自动装配的类 都不使用 代理bean 方法；
@LoadBalancerClients 这个注解的作用，@Import(LoadBalancerClientConfigurationRegistrar.class) 这块的细节问题后面探索


LoadBalancerProperties: spring.cloud.loadbalancer.healthCheck 主要用于 HealthCheckServiceInstanceListSupplier
用于健康检查啊, 默认delay=0, interval: 25s
主要创建LoadBalancerClientFactory， 这个负载均衡客户端工厂的bean.

主要看cache.
spring-context定义了一个cache包下， interface Cache, 

CacheManager SPI {
	根据名称获取Cache
	获取所有可以获取德CacheName
}

Cache {
	两个属性：
	cacheName;
	cacheProvider;
}

CacheAutoConfiguration 这个是spring的Cache 自动配置，没啥精力了先放在这把这个。























































===============================================================================================================================>>
写一套高可扩展 稳定的 支付服务，先封装功能，如果有共性先共性设计，就算是这段时间架构封装设计的一个实现

目的就是达到让starter一样 只需要引入简单的starter jar包, 然后通过外部简单的配置就能执行支付能力
首先实现微信和支付包的支付功能： 
支付包： 看下第二版本和
微信：

要提供扩展方法和聚合方法：
===============================================================================================================================>>







===============================================================================================================================>>
===============================================================================================================================>>
===============================================================================================================================>>


===============================================================================================================================>>
Spring Security 的探索
===============================================================================================================================>>
看官网：
imperative and reactive applications. 支持命令式和响应式编程，提供认证和授权以及防止普通的攻击。
它的目的就是所有的操作都在它自己的容器内部。？ 这个后面看下：

1. 认证，你是谁？ 需要存储用户的信息，所以首先需要存储用户的密码, 就抽象出一个密码加密器 PasswordEncoder
spring对这个接口进行了多样实现，并且描述了密码演进的历程，并告诉用户在5.0版本之前使用NOOP的加密器，但是应该使用特殊的加密器
但是又分析 老系统有些还在用NOOP,新的密码存储方式可能还会严禁，作为框架不能老是修改代码， 引入一个类： DelegatingPasswordDecoder
这个类解决了上面3个问题， 可以让系统使用当前的加密器完成加密，可以适应加密技术的升级，可以无感升级。 可以使用工厂创建这个对象。
然后看下代码：
final PasswordEncoderFactories.createDelegatingPasswordEncoder() 这个加密器的设计还是很简单的.
默认是使用这个类，但是我们内存中其实不需要维护那么多数据，为了内存, 我们手动实例一个bean吧。

2. csrf 公司。 跨域攻击。
你浏览器有cookie, 但是恶意网站的js 会利用你的cookie发起请求。 web防止跨域，但是app端不需要。 
恶意的人不能直接获取你的cookie自己去操作，仅仅是 借助浏览器 对请求资源 请求。 

3. http协议 请求头的定制
cache control: 缓存，默认不提供缓存，登出之后需要重新登录
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0 
等等定制http 头的部分


下面看 Spring Security 的包结构 https://docs.spring.io/spring-security/site/docs/5.4.6/reference/html5/#modules
1. spring-security-core.jar
org.springframework.security.core
org.springframework.security.access
org.springframework.security.authentication
org.springframework.security.provisioning

2. 








1. 我们一杆子引入了  spring-boot-starter-security 显然这个是 boot版本啊 
看下它的自动配置：  注意这种配置类
===========> org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
逐个分析这个配置：
1.1 @Configuration(proxyBeanMethods = false) 这个是公共的，具体怎么操作的还没有探索
1.2 @ConditionalOnClass(DefaultAuthenticationEventPublisher.class)
org.springframework.security.authentication.DefaultAuthenticationEventPublisher core包下面
看下为什么需要这个类？

首先需要了解： 应用事件发布器，这个接口定义  发布事件  的行为。
ApplicationEventPublisher： 这个顶级接口
ApplicationContext继承了这个类, 所有我们默认的ApplicationContext容器默认就是一个 应用事件发布器
publishEvent(Object event): 定义了一个接口 发布事件, 传入一个事件对象, 对事件进行抽象

==>然后我们要了解的这个core包下的 DefaultAuthenticationEventPublisher 默认的 认证事件发布器
它实现了 ApplicationEventPublisherAware 接口，所以它需要维护一个 ApplicationEventPublisher
这目的就很明显了 Auth 实际上 把 事件委托给  应用事件发布器 完成事件发布， 让 相应的Listener完成自己的业务处理。
这个类还实现了 AuthenticationEventPublisher, 主要发布认证成功，认证失败两个接口。

认证我们还需要了解一个接口： Authentication 暂时先把这个类放一边。
java定义了 Principal [ˈprɪnsəpl] 这个就是主角，可以代表一个重要的主体人，当事人，可以代表任意一个
实体，个人，公司，或者登陆id等。  getName()，这个接口没有时间 Serializable, 无法序列化、
Subject: 这个接口 主体，

1.3 @EnableConfigurationProperties(SecurityProperties.class)
这个配置类中有一个默认的User. Filter空的, 账号user密码uuid 所以可以配置。 不配置自动生成。 当时刚导入这个包的
时候真的恶心到了这点。
妈的为什么我的application.yml不提醒了, 我已经引入 processor 包了啊？？？？ 我重建文件系统索引 和 内存里面的索引试试 这个真的很让人烦啊。可以了

1.4 @Import({ SpringBootWebSecurityConfiguration.class, WebSecurityEnablerConfiguration.class,
		SecurityDataConfiguration.class })   这个主要看 @Import注解 但是现在还没有看明白啊
SpringBootWebSecurityConfiguration： 
WebSecurityEnablerConfiguration：
SecurityDataConfiguration：

===========> org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
这个自动配置的作用就是 初始化一个内存相关的  认证管理员   
----->  UserDetailsService 主要是这个服务  UserDetails-User, 
这里有三个bean需要了解下
1.1 AuthenticationManager :
1.2 AuthenticationProvider :
1.3 UserDetailsService : 

ConditionalOnMissingBean : org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
缺失上面这个类才会走这个内存, 那么这 oauth2 的这个类暂且先不看。
默认用户存储是内存操作: InMemoryUserDetailsManager(UserDetails... users) 并且是懒加载模式。

我们可以看到一个类： SpringBootWebSecurityConfiguration 它是一个内部类：
里面有一个bean: SecurityFilterChain(挺关键的)
这个类的优先级很低，目的在于如果用户自定义了 SecurityFilterChain 这个就不进行初始化了，就默认使用用户自定义的。

目前又出现了一个关键类： HttpSecurity  它的设计可以具体看下， 类图不是很复杂，而且这是一个final, 看下这个类的设计。

































































HttpClientConfiguration : cloud的httpClient的规范, 自动加入了OkHttp的jar包
========================================================================================>>
spring.cloud.httpclientfactories.apache.enabled=false  # 默认是true,关闭apache的http工厂链接
spring.cloud.httpclientfactories.ok.enabled=true  # 默认是true, 会自动装备OkHttpClientConnectionPoolFactory，仅仅是一个工厂没有参数配置
OkHttpClient.Builder，直接是一个builder
OkHttpClientFactory直接创建builder, disableSslValidation 关闭ssl校验





========================================================================================>>
FeignAutoConfiguration: openFeign的自动化配置

feign.httpclient.*  实际是对okHttp的属性进行配置 具体看这个类 FeignHttpClientProperties
@ConditionalOnClass(OkHttpClient.class)
@ConditionalOnMissingClass("com.netflix.loadbalancer.ILoadBalancer")

@ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
@ConditionalOnProperty("feign.okhttp.enabled")

它会自动装配链接池，创建okHttpClient, 创建FeignClient.

feign.okhttp.enabled=true.
feign.httpclient.enabled=false. 默认是true. 

========================================================================================>>
FeignRibbonClientAutoConfiguration: Ribbon的自动装配、

ILoadBalancer： 这个是ribbion的顶级抽象接口。 获取服务器的抽象列表。 
@ConditionalOnMissingClass("com.netflix.loadbalancer.ILoadBalancer")


OpenFeign中对ribbion的自动配置：
@ConditionalOnClass({ ILoadBalancer.class, Feign.class })
@ConditionalOnProperty(value = "spring.cloud.loadbalancer.ribbon.enabled",
		matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(FeignAutoConfiguration.class)
@EnableConfigurationProperties({ FeignHttpClientProperties.class })
// Order is important here, last should be the default, first should be optional
// see
// https://github.com/spring-cloud/spring-cloud-netflix/issues/2086#issuecomment-316281653
@Import({ HttpClientFeignLoadBalancedConfiguration.class,
		OkHttpFeignLoadBalancedConfiguration.class,
		DefaultFeignLoadBalancedConfiguration.class })

1.如果classPath下存在bibbon的顶级接口，也有Feign.class 
2.spring.cloud.loadbalancer.ribbon.enabled 默认这条等于 true.
3.这个类的 是在Feign的自动装配前面完成的
4.FeignHttpClientProperties 也需要这个配置类的属性
5.对HttpClient完成负载均衡能力


--------------------------------------------------------------------------------------------------------
@ConditionalOnClass(Feign.class)
@ConditionalOnBean(BlockingLoadBalancerClient.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
@AutoConfigureAfter(FeignRibbonClientAutoConfiguration.class)
@EnableConfigurationProperties(FeignHttpClientProperties.class)
@Configuration(proxyBeanMethods = false)
// Order is important here, last should be the default, first should be optional
// see
// https://github.com/spring-cloud/spring-cloud-netflix/issues/2086#issuecomment-316281653
@Import({ HttpClientFeignLoadBalancerConfiguration.class,
		OkHttpFeignLoadBalancerConfiguration.class,
		DefaultFeignLoadBalancerConfiguration.class })

1.classpath下存在Feign.class
2.BlockingLoadBalancerClient 必须存在这个bean才可以
3.在 Feign 配置之前， 在ribbion的配置之后
4. ... 其他就是通用的一些操作
重点看： BlockingLoadBalancerClient 这个类 就是 Spring-cloud-loadbalancer 对springCloud的具体实现。

ServiceInstanceChooser 这个是SpringCloud定义的顶级接口. 提供负载均衡选择 一个服务实例
ServiceInstance choose(String serviceId);

LoadBalancerClient继承了 ServiceInstanceChooser 提供其他能力, 负载均衡客户端，提供了请求的能力。
然后其他负载均衡具体实现了这个接口。

但是loaderBalancer存在bug, https://github.com/spring-cloud/spring-cloud-commons/pull/890
--------------------------------------------------------------------------------------------------------
spring-cloud-loadbalancer-starer 的相关配置文件, 每个starter都有自己的自动装配的对象


========================================================================================>>
analogous to [əˈnæləɡəs]  和...类似

========================================================================================>>
探索一下@Configuration的使用
先看下这个注解：
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {
	// 默认是代理bean方法，这个属性的含义具体是什么，后面使用。
	boolean proxyBeanMethods() default true;
}

这个注解贯穿整个Spring的设计核心。
与它相关的有 @Component, @Bean, @Profile, @Import,
@ImportResource, @ComponentScan, @Lazy
@PropertySource, 
AnnotationConfigApplicationContext,
ConfigurationClassPostProcessor,
Environment,
ContextConfiguration.


1. 首先看一下 注解配置的 应用上下文这个核心类AnnotationConfigApplicationContext
AnnotaionConfig  + ApplicationContext.

1> ApplicationContext是一个抽象出来的应用上下文系统接口
它继承了 一个顶级 BeanFactory 的能力接口, 说明它本身就是 Bean 工厂，这个应用上下文可以生产bean.
这个接口仅仅暴露了获取所有bean的能力， getBean() 这个方式实际就是创建bean的方法，同时提供了其他
判断bean的一些读取能力。

继承了 EnvironmentCapable, 它能获取一个 环境的对象.
继承了 ApplicatonEventPubliser, 它具备发布事件的作用.

ApplicationContext:
	- Environment

2> 我们需要看的第二个抽象类： GenericApplicationContext
它首先是一个 ApplicationContext,
然后它还扩展了 BeanDefinitionRegistry, 说明它就是一个BeanDefinition的注册中心，
这里说下 Registry 注册中心 提供了全面的 BeanDefinition 读写能力 ~！~.
所有bean的定义都会注册在它这里, 它有一个内部属性：
GenericApplicationContext:
	- DefaultListableBeanFactory.

2-1> 我们需要看DefaultListableBeanFactory这个核心类：
这个类具体的类图需要具体看源码, 以及这个类的 Structure, 可以看清这个类的实现细节。
简单说一下，这个类实现了
BeanFactory 它是一个生产bean的工厂
BeanDefinitionRegistry 所有的bean的定义都会存储在这个类里面
SingletonBeanRegistry  所有的生产的bean都会存储在这个单例bean的容器里, 能力提供了单例bean的读写能力。
==> Spirng IOC 的核心都会在这个类里 DefaultListableBeanFactory 它负责存储Bean的定义，
生产bean的工厂，以及把bean生产出来，然后保存在本身。
因为它也聚合了很多的接口，所以他也暴露了很多的接口。

2-2> 我们看最终的我们需要探索的一个类 AnnotaionConfigApplicationContext, 这个类首先是一个
GenericApplicationContext, 这个抽象通用的application完成了大多数的功能，但是这个 AnnnotationConfig
前缀提供了注解能力， 它实现了一个 AnnotationConfigRegistry, 为了实现这个类提供了两个实现类完成特定的功能
一个 AnnotatedBeanDefinitionReader 被注解标记的 bean定义 读取器
一个 ClassPathBeanDefinitionScanner classPath下bean定义的扫描器
简单来说，就是一个读取器 一个扫描器。




# Spring
spring的几大基础包， core, beans, context, aop, expression, web, tx. 等这些基础包


Spirng的重要方法, AnnotationConfigApplicationContext的 register(clazz): 把class封装成beanDefiniton.
refresh() 方法： 就是开始创建对象了。
AbstractApplicatonContext: 这个抽象的应用上下文 也是非常的重要，它用一个辅助的类： PostProcessorRegistrationDelegate
这个类提供了一些静态方法用来处理 postProcessor.

org.springframework.context.annotation.internalConfigurationAnnotationProcessor: 这个是内置的配置注解处理器。 具体的作用后面再看看。 

org.mybatis.spring.mapper.MapperScannerConfigurer



==============================================================================================================>>
org.springframework.boot  spring-boot-autoconfigure 这个包里面都是自动装配 这个装配非常强大，简单的配置一下即可。、

后面看看redis相关的动态配置。






org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration,\






==============================================================================================================>>
Git 相关的版本操作要炉火纯青， 是本地的一个客户端程序。 和服务端链接用于版本控制。 
==============================================================================================================>>
常用的命令  宏观的概念  
概念： 
workspace: 工作区间，就是我们写代码的环境，直接编辑，这个就是和 .git 目录同级的我们的资源。
stage(cache): 缓冲区， 暂时存储代码的地方，待提交区, .git下 index文件，记录了文件索引的目录，记录了文件的状态，依赖对象库、
local: 本地仓库 local repo, Head文件存储，都公用了 Object, git对象库。
remote: 远程仓库， 就是统一的代码托管仓库，通过集中的管理。

git init: 在随便一个工作空间就是目录创建一个.git文件 这就代表生成一个版本控制，这个目录下所有资源都被控制 23K
在工作空间创建了新的文件，
git add . 把当前目录下的变动文件提交到 stage. 暂缓区， 
	git status 可以查看 缓存区 的文件，变动的文件，  具体查看就是 git diff 可以看到具体的变动。 
git commit -m 'mark' 把暂缓区的提交到 本地仓库 local repo.  如果没有git add . 的话 是不让直接commit的，可以使用 -a 自动add
git push: 才会把本地的代码推到远程仓库 如果是第一个push  git push -u <remote> <branch> 然后就会自动绑定不用添加其他参数直接可以
	git pull 和 git push. 但是如果你是手动创建了远程仓库，已经存在了一些文件，直接push不被允许，让你先pull同步数据。
	使用 git pull <remote> <branch> 同步  fatal: refusing to merge unrelated histories 然后又报错，说不能拉取merge不相关的历史。
	git push -u origin master  --allow-unrelated-histories 可以使用这个后缀允许两个不同仓库进行merge.
	然后git log 可以看出 Head--> 当前分支和远程分支，说明两个分支的头已经同步。
	切换到我的分支，git push 发现提示，我的分支没有上流分支，让我push当前分支，然后使用远程分支做当前分支的上游，
	git push --set-upstream origin jinyun. 这样就完成了远程分支和本地这个分支的数据同步，说明三个都同步了。


git pull: 是把远程仓库的代码拉取到工作空间。 这样就构成了一个闭环。 如果我们本地的.git 没有关联一个远程分支，就不能使用这个命令
	而且会告诉你  git remote <remote> <branch>
	
连接远程：
git remote -v ：查看远程版本库信息， 如果没有关联过，就不会有任何远程信息
git remote add <remote： 你给远程仓库起的一个名字，一般我们都起 origin, 起源，身世，源于的意思> <url：这个就是远程仓库真正的地址>： 把当前仓库和远程仓库关联起来
这个 remote 就代表远程主机 的一个资源标识，后面你直接可以使用这个别名来进行远程操作。

然后我们查看就可以看到 连接到远程的两个 fetch(拉去代码， 远程仓库到), push(往上面推代码，本地仓库到远程仓库)
git fetch <remote> <branch>（： localbranch）: 把某台主机的某个分支的代码拉去到本地 后面的：本地分支。
git fetch 就是把远程的最新代码拉去到本地分支，不会自动merge, 需要手动完成merge, 不如git pull快捷。



查看：
git log: 查看当前分支的提交记录， 最上面的就是head-> master

分支： （重要）
git branch:  查看本地所有分支
git branch [newBranchName]: 创建新的分支，创建的那一刻 两个分支的代码是一样的，一旦切换，就是新的分支管理当前的代码。 Head -> 这两个分支
	但是只要有一个分支修改，这个分支的头就会自动递增，head指向新的。
	一旦切换分支，代码内容文件内容直接发生变化的。
	一旦两个分支同时修改了相同的文件，就会出现冲突，这时候需要手动完成merge. 不同文件的话，首先git会选择自动merge.
	会出现(branch|MERGING) 这样的标记，这就表明出现了冲突，需要手动解决冲突， git status, 就可以查看你当前的分支存在没有merged的文件
	git merge --abort: 这个命令出现 丢弃merge. 还会展示冲突的文件。 head执行最新一次的提交。
	直接打开文件 就可以看到<<<head =====     >>> master 这样的符号， 把这个符号按照自己的需求删除掉即可。
	然后commit, 之后，merging消失， 然后我们切换到 分支，看到那个分支的代码没有变化。
	没有变化，我们直接 使用 git merge branch, 会发现git会自动merge.    head-->这两个分支同步了。

git checkout [branch]: 切换对应的分支，  git branch -d [branch]: 删除本地分支
git merge branch, 把指定的分支的代码合并到当前分支。


版本回退：
正常情况，我们本地的开发分支和本地master 和远程master都是同步的， 我们在个人分支开发代码，然后merge到本地master, 统一使用本地master分支向
远程仓库 dev分支合并代码，这时候我们出现bug, 需要回退代码操作。

标签： （基本没有用过，暂时不看）

相应的场景。
看看官网。 https://git-scm.com/docs

1> 创建一个仓库 git repository
2> 分支的概念，一般仅仅创建一个master分支， 这里我们创建， 我们测试git的功能特性，使用简单的几个文件即可。


基础操作
git --verison:  查看git版本
==============================================================================================================>>


backing up files: 备份文件




==============================================================================================================>>
https://docs.spring.io/spring-framework/docs/5.2.13.RELEASE/spring-framework-reference/web.html#websocket
Web on Servlet Stack.

WebSocket的自动配置 我们使用的是Undertow的ServletWebServer 内置容器;
AbstractMessageBrokerConfiguration:
这个抽象的 消息代理 配置类：提供必要的配置 用于处理 stomp 协议的消息；
1. 首先它实现了 ApplicationContextAware接口，所以它获取了ApplicationContext的所有暴露接口
2. 它还暴露了这个ApplicationContext
3. 


==============================================================================================================>>
开发的服务：
im服务创建成功push;
kibana 日志中心玩了下非常流弊；
redis 测试 redis组件














---------------------------------------------------------------------
Linux
-----------------------------------------------

大数据服务器： 8core 32G

------------------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------------------


一、查看系统信息

# uname -a # 查看内核/操作系统/CPU信息

# head -n 1 /etc/issue # 查看操作系统版本

# cat /proc/cpuinfo # 查看CPU信息

# hostname # 查看计算机名

# lspci -tv # 列出所有PCI设备

# lsusb -tv # 列出所有USB设备

# lsmod # 列出加载的内核模块

# env # 查看环境变量
# export # 设置环境变量 win:set


二、查看资源信息

# free -m # 查看内存使用量和交换区使用量

# df -h # 查看各分区使用情况

# du -sh <目录名> # 查看指定目录的大小

# grep MemTotal /proc/meminfo # 查看内存总量

# grep MemFree /proc/meminfo # 查看空闲内存量

# uptime # 查看系统运行时间、用户数、负载

# cat /proc/loadavg # 查看系统负载

三、查看磁盘和分区信息

# mount | column -t # 查看挂接的分区状态

# fdisk -l # 查看所有分区

# swapon -s # 查看所有交换分区

# hdparm -i /dev/hda # 查看磁盘参数(仅适用于IDE设备)

# dmesg | grep IDE # 查看启动时IDE设备检测状况



四、查看网络信息

# ifconfig # 查看所有网络接口的属性

# iptables -L # 查看防火墙设置

# route -n # 查看路由表

# netstat -lntp # 查看所有监听端口

# netstat -antp # 查看所有已经建立的连接

# netstat -s # 查看网络统计信息


五、查看进程信息

# ps -ef # 查看所有进程

ps 进程查看器：

Linux Tools Quick Tutorial

# top # 实时显示进程状态

























==============================================================================================================>>
Spring 创建对象最核心的一块代码：







==============================================================================================================>>
taskkill /f /t /im nginx.exe










