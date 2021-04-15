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
			- MetricHttpAgent 它也需要一个干活的人，然后它负责扩展了一些能力。
			- 使用了包装设计模式，这里看看这个设计模式的好处和类图，设计的动机。  
			motivation. --motive: mot ive:n 表示动机  intention,意图，打算，计划， cause n 也是原因动机的意思。 cuz 口语的原因，动机的表述，使得也是这个。




3.3> nacosConfigProperties.timeout 获取配置的超时时间(3000) name: dataId name.
name, dataIdPrefix=null, 把name赋值给prerfix, 如果还空，获取environment的property("spring.application.name")


4> @Refreshed 注解，这个注解非常强大  可以实现配置的动态加载配置， 后面可以探究一下这个注解的实现逻辑。





===============================================================================================================================>>
===============================================================================================================================>>
===============================================================================================================================>>
探究 springboot 解析 bootstrap.yml 配置文件的过程。 具体是怎么完成解析的。 这个在2.4.0版本 这个地方改动很大，先不看这个版本。