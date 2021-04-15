package org.example.feature.function;

import org.example.model.thread.executor.service.ThreadPoolExecutorTest;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 学到了 spring web 中一种调用方法
 * <p>
 * java8的特性： 方法称为第一公民；只要方法的 出入参定义的确定了 对于 @InterfaceFunctional
 * 用起来真的是他妈的方便啊 就是随便用
 * 后面项目中 先强制的先 试试用着 后面才逐渐体会它应该用在什么地方。
 * <p>
 * 这种写法其实写线程的时候就用到了啊，妈的，怎么刚才还不通窍呢？
 *
 * @author: Liu Jinyun
 * @date: 2021/4/4/2:26
 */
public class LearnTest {
    public static final ThreadPoolExecutor executor = ThreadPoolExecutorTest.threadPoolExecutor;

    public static final Runnable r = () -> {
        // 线程需要的计算方法, 线程调用的原始 规范
        // 处理计算 没有返回结果 没有参数
        // 所以需要定义的任务的 总体类进行封装 既然方法中没有那就需要从成员变量或者全局变量中进行获取或者计算。
        // 注意一点：如果线程中涉及到 redis IO 或者 mysql的 IO 这个性能怎么评估?
        // todo: 如果线程中涉及到 redis IO 或者 mysql的 IO 这个性能怎么评估?
    };

    public final Callable<Integer> c = () -> {
        // 进行计算 只能提交给线程池
        return 1;
    };


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Integer> future = executor.submit(r, 1);
        System.out.println(future.get());
    }





    // 我们一般使用线程是 第三方接口 通过以后的上面的局部变量在线程中直接传入数据 调用带有参数的方法 这是
    // 最low的线程使用方法, 也是最普遍的使用线程的方式
    // 如何利用多线程完成并行计算, 充分发挥多核处理器的极限性能是值得考虑的问题？
    // todo: 如何利用多线程完成并行计算, 充分发挥多核处理器的极限性能是值得考虑的问题？


    interface ContextConfig {

    }

    interface ContextInitializer {
        void init(ContextConfig c);
    }


    void arbitraryMethod(ContextConfig c) {
        System.out.println("我其他任务定义的方法 只要传入的这个值是 对应的类型, 其他我不问");
    }

    ContextInitializer getInitializer() {
        return this::arbitraryMethod;
    }

    ContextInitializer get2() {
        return k -> arbitraryMethod(k);
    }
}
