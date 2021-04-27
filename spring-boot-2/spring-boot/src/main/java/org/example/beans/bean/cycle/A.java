package org.example.beans.bean.cycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 如果直接使用 构造方法 的情况 循环依赖无法解除<   可以只用注解注入的方式注入;
 *
 * <p>
 * 只有一个默认的构造方法, 没有无参的构造方法
 * 通过默认的构造方法, 然后通过属性注入的方式 实例化对象的区别?
 *
 * @author: jinyun
 * @date: 2021/4/27
 */
@Service
public class A {
    @Autowired
    private B b;

}
