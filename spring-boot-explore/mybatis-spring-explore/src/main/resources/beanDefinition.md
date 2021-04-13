1. ImportBeanDefinitionRegistrar.  2021-3-3




2. AliasRegistry: 别名注册中心
   Common interface for managing aliases. Serve as a super-interface for BeanDefinitionRegistry.
   
   Implementation use a map from alias to canonical name. Serve as a base implementation for BeanDefinitionRegistry.

3. SingletonBeanRegistry: 单例bean的注册中心
Interface that defines a registry for shared bean instances. 这个接口定义了一个注册中心 提供 共享bean.
   in order to expose their singleton management facility in a uniform manner. 
   

javadoc: <li> <p> <b> {@link } 


### English Javadoc Keywords:
1. Fully Qualified name, Plain class name.
2. in a uniform manner.
3. Return if a registration has been registered in the registry.
4. Return if a registration exists for the given type.
5. Serve as a {base/root/super/generic/common} {class/interface} for {objective/target/aim/goal}.