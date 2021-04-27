package org.example.beans.bean.cycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The dependencies of some of the beans in the application context form a cycle:
 *
 * ┌─────┐
 * |  a defined in file [E:\learning\framework\spring-boot-2\spring-boot\target\classes\org\example\beans\bean\cycle\A.class]
 * ↑     ↓
 * |  b (field private org.example.beans.bean.cycle.A org.example.beans.bean.cycle.B.a)
 * └─────┘
 *
 *
 * 回答：循环依赖就是循环引用，就是两个或多个Bean相互之间的持有对方，比如CircleA引用CircleB，CircleB引用CircleA，则它们最终反映为一个环。
 * Spring如何解决循环依赖？ 
 * 假设场景如下，A->B->A 
 * 1、实例化A，并将未注入属性的A暴露出去，即提前曝光给容器Wrap
 * 2、开始为A注入属性，发现需要B，调用getBean（B）
 * 3、实例化B，并注入属性，发现需要A的时候，从单例缓存中查找，没找到时继而从Wrap中查找，从而完成属性的注入
 * 4、递归完毕之后回到A的实例化过程，A将B注入成功，并注入A的其他属性值，自此即完成了循环依赖的注入
 *
 * spring中的循环依赖会有3种情况：
 * 1.构造器循环依赖
 *     构造器的循环依赖是不可以解决的，spring容器将每一个正在创建的bean标识符放在一个当前创建bean池中，在创建的过程一直在里面，如果在创建的过程中发现已经存在这个池里面了，这时就会抛出异常表示循环依赖了。
 * 2.setter循环依赖
 *    对于setter的循环依赖是通过spring容器提前暴露刚完成构造器注入，但并未完成其他步骤（如setter注入）的bean来完成的，而且只能决定单例作用域的bean循环依赖，通过提前暴露一个单例工厂方法，从而使其他的bean能引用到该bean.当你依赖到了该Bean而单例缓存里面有没有该Bean的时候就会调用该工厂方法生产Bean，
 * Spring是先将Bean对象实例化之后再设置对象属性的
 * Spring先是用构造实例化Bean对象，此时Spring会将这个实例化结束的对象放到一个Map中，并且Spring提供了获取这个未设置属性的实例化对象引用的方法。
 *
 * 为什么不把Bean暴露出去，而是暴露个Factory呢？因为有些Bean是需要被代理的
 *
 * 3.prototype范围的依赖
 * 对于“prototype”作用域bean，Spring容器无法完成依赖注入，因为“prototype”作用域的bean，Spring容器不进行缓存，因此无法提前暴露一个创建中的Bean。
 * ————————————————
 * 版权声明：本文为CSDN博主「hezuo1181」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/hezuo1181/article/details/82831080
 *
 * @author: jinyun
 * @date: 2021/4/27
 */
@Service
public class B {

    /**
     * @Autowired 可以标记 构造方法， 成员字段， 方法， 方法参数等
     *
     * 看下这个的规范;
     */
    @Autowired
    private A a;
}
