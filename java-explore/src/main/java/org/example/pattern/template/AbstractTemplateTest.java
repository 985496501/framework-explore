package org.example.pattern.template;

import org.junit.Test;

/**
 * 基于抽象类的模板设计模式， 把逻辑骨架放到 抽象类中实现，但是把 算法中的需要的基础数据结构等进行抽象
 * 由具体的 实现 直接 继承 这个抽象基类 具体实现即可，这个抽象类完成记忆基础数据构建。
 *
 *
 * 原来我遇到这种设计实现其实 是 Spring Security 的实现， 后来我模仿着它 写了一个合同的starter包
 * 就是定义顶级接口 以及抽象接口供子类实现, 子类只要实现就能完成装配, 后面看到 RestTemplate 这个也使用了 模板方法。
 * 看到类图和设计才发现 原来真的就是这样。
 *
 * @author: jinyun
 * @date: 2021/3/12
 */
public class AbstractTemplateTest {

    static abstract class AbstractTemplate {

        public abstract void methodA();
        public abstract void methodB();

        public void method() {
            methodA();
            System.out.println("抽象的业务逻辑");
            methodB();
        }

    }

    static class ConcreteTemplate extends AbstractTemplate {

        @Override
        public void methodA() {
            System.out.println("我是具体子系统");
        }

        @Override
        public void methodB() {
            System.out.println("我是具体子系统");
        }

        public void selfMethod() {
            method();
        }
    }


    @Test
    public void templateTest() {
        ConcreteTemplate concreteTemplate = new ConcreteTemplate();
        concreteTemplate.selfMethod();
    }

}
