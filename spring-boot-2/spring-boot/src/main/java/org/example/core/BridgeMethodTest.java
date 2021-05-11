package org.example.core;

import org.junit.jupiter.api.Test;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>桥接方法：</p>
 * flag:  ACC_PUBLIC;  ACC_BRIDGE;  ACC_SYNTHETIC  synthetic, 合成的,
 * <p>子类重写父类的方法, 返回类型是 父类返回类型的子类型, 编译器会帮助保证重写的一直性 (参数和返回值完全一样), 会帮助重写方法, 然后会自动调用我们
 * 写的重写方法;
 * <p>重写泛型方法, 生成桥接;
 *
 *
 * @author: jinyun
 * @date: 2021/5/8
 */
public class BridgeMethodTest {

    @Test
    public void inspectTest() {
        A a = new B();
        Method build = ClassUtils.getMethod(A.class, "build");
        System.out.println(build);

        Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(build);
        System.out.println(bridgedMethod);
    }



    // ----子类重写父类的泛型方法 --------------------------

    @Test
    public void syntheticMethodTest() {
        Evaluation e = new VIPEvaluation();
        e.evaluate(new VIP());
//        e.evaluate(new NoVIP());
    }


    interface Customer {
        void consume();
    }

    /**
     * nested inner class, BridgeMethodTest$VIP named.
     */
    class VIP implements Customer {
        @Override
        public void consume() {
            System.out.println("VIP消费....");
        }
    }


    class NoVIP implements Customer {

        @Override
        public void consume() {
            System.out.println("NO VIP消费....");
        }
    }

    abstract class Evaluation<C extends Customer> {
        void evaluate(C c) {
            c.consume();
        }
    }


    class VIPEvaluation extends Evaluation<VIP> {
        @Override
        void evaluate(VIP vip) {
            super.evaluate(vip);
        }
    }













    // ----重写方法, 返回值的类型是 父类的子类 --------------------------

    @Test
    public void ATest() {
        A a = new B();
        a.build();
    }

    class A {
        public Executor build() {
            return null;
        }
    }


    class B extends A {

        @Override
        // 会自动生成这个方法 complier.
//        public Executor build() {
//            return this.build();
//        }

        public ExecutorService build() {
            return Executors.newSingleThreadExecutor();
        }
    }
}

