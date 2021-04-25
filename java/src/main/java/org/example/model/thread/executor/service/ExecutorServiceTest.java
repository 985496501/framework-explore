package org.example.model.thread.executor.service;

import org.example.model.thread.util.Sleeper;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * {@link ExecutorService} 主要是这个接口的方法定义探索
 *
 * @author: jinyun
 * @date: 2021/4/25
 */
public class ExecutorServiceTest {
    /**
     * // todo: 抽取到 concurrent package 下;
     * {@link java.util.concurrent.Executor} 执行器, 执行提交的任务, 屏蔽了你用哪个线程执行任务, 具体调度的细节;
     * {@link java.util.concurrent.ExecutorService} 执行器服务; 它提供了关闭方法, 当这个不需要的时候需要关闭释放资源;
     * shutdown(): 一旦执行就不允许提交任务,
     * shutdownNow(): 所有的任务包括现在正在执行的任务都不会执行了;
     *
     * 同时引入了{@link java.util.concurrent.Future}能够追踪n个异步任务的结果; submit(): 提供了一个Future类可以用于任务的取消和结果的等待获取
     */
    @Test
    public void executorServiceTest() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Runnable task = ()-> {
            System.out.println("我要开始执行任务了....");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // 这就明白了这个中断异常的情况, 因为这个方法让线程阻塞了, 需要外接其他线程打断它让他继续执行
                // 还是立即返回 这个完全有任务本身决定 原来是在这啊
                // return;
                System.out.println("有人打断了我, 我可以继续执行了");
            }

            System.out.println("任务执行结束....");
        };

        java.util.concurrent.Future<?> future = executorService.submit(task);
        // 服务停止, 任务完成 或者 停止 或者异常都是 true
        try {
            Object o = future.get(1, TimeUnit.SECONDS);
        } catch (ExecutionException | TimeoutException e) {
            // 我们要求任务在1s内必须有返回结果 实时操作系统 的感觉：
            System.out.println("1s内我没有拿到结果, 我直接取消任务了... timeout...");

            // 如果这个任务还没有执行调用这个方法会让这个任务永远都不会执行, 如果已经执行则取决于参数
            // todo: 是否 中断, 这个中断 是 操作系统中非常非常 重要的概念;
            // false: 不中断 任务继续执行  true: 中断 强制任务结束
            future.cancel(true);
        }

        boolean done = future.isDone();
        System.out.println(done);

        TimeUnit.SECONDS.sleep(3);
        executorService.shutdown();
    }

    /**
     * 有结果的任务, Runnable 没有任何结果返回, jdk1.5之后加入
     * 可以有任务结果返回的接口, 同时配合 Executors 使用;
     */
    @Test
    public void callableTest() throws ExecutionException, InterruptedException {
        // 实际上就是创建一个包装对象啊 这个 这个就是我们给什么 就返回什么
        // Callable<Class<String>> hello = Executors.callable(() -> System.out.println("hello"), String.class);
        // 实际上我们是包装一个方法 有参数 有返回值的
        int i = 3, j = 5;
        Callable<Integer> callable = ()-> add(i, j);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> submit = executorService.submit(callable);
        Integer integer = submit.get(); // block
        System.out.println(integer);

        executorService.shutdown();
    }

    /**
     * 模拟一个耗时的计算, 如果纯计算 线程的数量不用太多
     * 如果是IO的就 设置多一点线程, 但是这个线程一旦IO, 就是自动调用BLOCK 原语让自己阻塞, 从而不会浪费CPU的时间片
     * 等得IO结束, 操作系统的产生事件, 可能是IO中断, 来唤醒阻塞的thread 到就绪状态, 重新竞争CPU的执行权;
     */
    private int add(int i, int j) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return i + j;
    }

    /**
     * {@link AbstractExecutorService}: 这个就是一个基类实现 执行器服务;
     * 默认实现了 ExecutorService的执行方法; submit(); invokeAll(); invokeAny()
     * 引入了一个接口 {@link RunnableFuture}; 它就是一个任务对象, 包装了最基本的任务和任务的返回值;
     * 这个对象 聚合了任务和结果, 既有任务又有结果, 可以执行它的任务, 然后把结果写到它的内存属性里面;
     * 这里默认就是我们给什么值 就返回了什么值; 而在实际的操作中, 我们是 异步获取计算的结果;
     *
     *
     * 这个抽象的服务, 提供了模板方法 RunnableFuture newTaskFor(Runnable, T) 这个默认方法; 默认是用了
     * {@link FutureTask}这个类作为默认实现, 同时它允许 子类覆盖这个方法, 自定义自己的任务;
     * FutureTask:
     * volatile int state; 1. 保证内存的可见性; 防止指令重排  todo: 如何保证, 既然是关键字, 那么编译器是怎么编译成OS保证的标识的呢?
     * 1. 创建这个任务的时候 state = NEW (0)  : 这个状态是多线程竞争的 所以被设计成原子操作, 多线程访问这个FutureTask都首先通过state判断;
     * outcome: 这个是结果任务, 因为它的前后被 state 原子操作包裹 不用 volatile;
     * runner: 这个也是线程竞争的, 多线程有可能抢占任务;
     * waiters: 这个是堆栈, 就是在堆中的 栈数据结构; 先进后出; 首先来的压栈? todo:
     *
     * 2. 直接看它的run(); 先判断任务是不是新建状态啊, 不是直接返回；
     *      是新建状态, 使用原子操作 为当前任务设置执行线程, 如果失败直接返回, 这里就体现了多线程竞争任务的代码;
     *      然后设置了ran标识 try 包裹任务方法, 人家也不知道你的任务成功还是失败;
     *      如果成功了把结果给result; 先原子操作 设置任务状态为 Completing, 然后设置值,
     *      这里使用原子操作是因为有其他线程进行get(), 也就是说 Completing 代表任务执行成功了,
     *      - 没有异常的发生,但是你get()是获取不到值的, 然后再进行normal设置, 这就是最终的完成状态了,
     *      最后调用一个 finishCompletion() 这个方法肯定是唤醒get() 请求的线程的; 我猜;
     *      果然如此; 唤醒操作: todo: 这个必须先看下 get();
     *      - 如果任务执行出现了异常, result=null; fan=false; setExeception(ex);
     *      还是原子操作锁住 任务完成, 然后 输出结果就是异常, 然后任务的状态就是 exceptional;
     *      同时唤醒 其他等待线程;
     *
     *      唤醒操作, 让堆栈的线程从头到尾 依次进行唤醒操作;
     *
     *      最后都会执行的是: runner 必须是非空 直到任务状态 被设置成final state状态, todo: ? 这个注释的意思
     *      然后查看 任务的状态 是不是 interupting 或者  interrupted 如果是后面就是取消任务的操作了 todo: ...
     *
     * 3. 我们看下get方法; 让获取线程直接进入堆栈的操作; 标准的并发编程的实现包：
     *      首先通过操作系统提供的 nanoTime 获取 deadline; 这个就是允许自旋获取的最后时间
     *      开始自旋;
     *      首先判断当前线程是否中断;  {@link Thread#interrupted()} 这个会调用native方法, 检查线程的中断标识位是否被中断
     *      如果是就清除 中断标识位; 然后在应用程序中继续抛出中断异常, 由应用程序自己处理中断;
     *      todo: 具体的实现后面再看吧；
     *      awaitDone(timed, nacos)
     *      具体就是如果状态是Completing就是让出CPU的执行权, 如果大于 Completing 就返回state; 首先是状态, 状态无法返回才会自旋;
     *      第一次自旋; q = null; 然后创建一个waitNode节点;
     *      第二次自旋; queued = false; 通过 compareAndSwapObject(this, offset, source, target)
     *      q.next = waiters, q; 原子操作头擦法 将最新的插入到 链表的头部;
     *      第三次自旋; 如果timed=false; 直接将当前线程park(this);
     *
     * Notes: {@link Future} 这个类提供了模板方法吧相当于, 因为它定义了最基本的任务的状态; 是Runnable, Future的封装;
     * 有任务 有执行, 而且还定义了执行任务, 读取任务状态的同步方法, 可以自定义 用户自己的任务?
     */
    @Test
    public void abstractExecutorService() {
    }


    /**
     * {@link ScheduledExecutorService} 它是个接口, 它定义了 ExecutorService 可以在某些延迟下, 周期性调用重复的任务,
     * execute(): 就是不延迟的执行任务;
     * schedule(): 一次执行
     * {@link ScheduledExecutorService#scheduleAtFixedRate(java.lang.Runnable, long, long, java.util.concurrent.TimeUnit)}
     * execute: initialDelay + period * n 固定的频率执行, 完全以时间为准
     *
     * {@link ScheduledExecutorService#scheduleWithFixedDelay(java.lang.Runnable, long, long, java.util.concurrent.TimeUnit)}
     * execute: 以结果获取的时间 然后重新开始 commence
     *
     * commence: 开始    start 的高级词汇；
     * proceed: 进行中
     */
    @Test
    public void scheduledServiceExecutorTest() {
        ScheduledExecutorService scheduledExecutorService
                = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleWithFixedDelay(() -> System.out.println("hello world"),
                0, 2, TimeUnit.SECONDS);

       // scheduledExecutorService.shutdown();
        Sleeper.sleep(10);
    }
}
