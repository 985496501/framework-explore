package org.example.pattern.adapter;

import org.junit.Test;

/**
 * 适配器模式的经典应用
 * spring的基础工程 spring-jcl 日志封装框架
 * 探究一下它是如何实现的？
 * <p>
 * 1. Target
 * 2. Adapter
 * 3. Adaptee
 * <p>
 * <p>
 * 现实情境：
 * 我有一个笔记本接口是Thunder3 雷电3接口
 * 但是我的手机是 TypeC接口
 * 两个之间插不上怎么办？
 * 我买了转换器 也就是适配器 把两个连接起来
 * <p>
 * 适配器的作用： 就是把两个不匹配的接口通过一个具体的object连接起来
 *
 * @author: jinyun
 * @date: 2021/3/15
 */
public class AdapterTest {

    /**
     * 定义两个接口一个是雷电3 一个是TypeC
     */
    public interface Thunder3 {
        void connectThunder3();
    }

    public interface TypeC {
        void connectTypeC();
    }

    public static class MacBookPro implements Thunder3 {
        @Override
        public void connectThunder3() {
            System.out.println("我是苹果, 我通过雷电3接口进行数据传输....");
        }
    }

    public static class OnePlus implements TypeC {
        @Override
        public void connectTypeC() {
            System.out.println("我是一加手机, 我通过TypeC接口进行数据传输....");
        }
    }


    // 已经模拟的现实的情况 那么我们要做什么 我们要让手机连接上我们的电脑

    public interface Target {
        void exchange();
    }

    /**
     * 我们买了一个两个头的适配器 连接两个设备
     * 其实这个 我也没太明白这个适配的作用
     * 但是要是让我实现 现实世界 我就这样实现
     */
    public static class Thunder3AndTypeCAdapter implements Target {
        Thunder3 thunder3;
        TypeC typeC;

        public Thunder3AndTypeCAdapter(Thunder3 thunder3, TypeC typeC) {
            this.thunder3 = thunder3;
            this.typeC = typeC;
        }

        @Override
        public void exchange() {
            thunder3.connectThunder3();
            typeC.connectTypeC();
        }
    }

    /**
     * 我们没有目标的平等描述了现实的世界的情况
     * 但是我们没有区分主体 软件设计 面向接口编程的目的 是给client提供高可用 高可扩展的接口服务
     * 比如我们以 External Client: MacBookPro
     * 我们需要定制一个接口 Target
     *
     */
    @Test
    public void mainTest() {
        MacBookPro macBookPro = new MacBookPro();
        OnePlus onePlus = new OnePlus();
        Target adapter = new Thunder3AndTypeCAdapter(macBookPro, onePlus);
        // 但是怎么做数据的传输呢？ 抽象一个适配器的接口Target
        adapter.exchange();
    }

}
