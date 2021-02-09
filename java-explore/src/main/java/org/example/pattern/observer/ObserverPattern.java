package org.example.pattern.observer;

/**
 * Observer Pattern:
 *
 * 这个设计模式有很多的变种:
 * publisher subscriber  发布者 观察者  也就是我们常用的 pub, sub 发布订阅模式
 * listener event:  监听器 也就是我们常说的 事件驱动模式
 *
 * 等等其他 还有传统的 观察者模式
 * 但是总归  他们都有核心:  就是一个事件的产生, 触发了一些列的事件响应
 * 事件的触发 ---> 一系列事件响应     1:n
 *
 * SpringApplication 的构建就是基于 事件发布的模式
 *
 * 采用这种方式的好处在于  可以扩展第三方系统的 集成
 * 首先 spring 定义了一个 订阅者的顶级接口 这个是基于应用的, 还有在这个顶层接口的子接口 还有很多
 * 当spring应用系统 触发了事件 就会回调所有的订阅这个事件的 订阅者 触发开发者自已定义的回调逻辑
 * 这样对于spring的开发者来说  就会忽略掉一些事件
 *
 * SpringApplication 系统内部是高内聚的, 系统内部都是接口传递, 不会轻易让 第三方系统 修改本系统的实现细节
 * merely expose interfaces in some case;
 *
 * 如果是基于spring开发可以 基于 Spring 的事件驱动模型 完成自己的 子系统的开发。 前提是基于spring的开发
 * 后面如果需要抽取来 就要自定义一个 事件驱动 模型
 *
 * netty 有事件驱动模型
 * spring 有事件驱动模型
 * 这个模型都是固定的，可以直接copy即可。
 *
 *
 *
 * @author: jinyun
 * @date: 2021/2/9
 */
public class ObserverPattern {
}
