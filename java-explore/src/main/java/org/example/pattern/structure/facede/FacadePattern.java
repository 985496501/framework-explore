package org.example.pattern.structure.facede;

/**
 * facade pattern: 门面设计模式
 * 当一个系统越来越复杂的时候, 有多个子系统, 定义了很多的顶级的子系统接口, 并且都各自实现了
 * 这时候 就会 定义一个 facade 门面接口 来统一封装 所有子系统的所有接口能力, 这样对于调用放来讲
 * 调用方  就不用关注 子系统的具体实现细节,  只需要关注一个 系统的门面接口即可。
 *
 *
 * Spring的  ApplicationContext 就是一个facade  它继承了很多子系统的接口。
 * 它相关的子系统: BeanFactory, MessageResource, ResourceLoader, ApplicationEventPublisher.
 *
 *
 * 一个接口其实就是一个子系统, 这是一个相对概念, 也可以看作是上面4个系统构成一个 ApplicationContext,
 * 一个ApplicationContext 同样是一个外层系统的子系统
 *
 * 这样就可以同通过接口完成 应用的 分层 设计
 * 让不同接口完成 不同的 层的服务能力
 * api
 * logic
 * fundamental
 * ...
 *
 *
 *
 * 门面设计模式的缺点：
 * 1. 即使你使用了 一个 统一的接口 来涵盖了 所有子系统的接口设计 但是你还是不能阻碍 调用方直接调用子系统的能力
 * 2. 如果你新增了一个 子系统 有极大的可能需要你 修改原来的代码
 * 这个设计模式在软件设计上  满足了一定的开闭原则  又一定程度上是违背开闭原则的。
 *
 *
 *
 * @author: jinyun
 * @date: 2021/2/9
 */
public class FacadePattern {







}
