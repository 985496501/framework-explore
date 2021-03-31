package org.example.thread.executor.aqs;

import org.example.thread.util.Sleeper;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * <h2>抽象的队列同步器 AQS</h2>
 * extends AbstractOwnableSynchronizer
 * A synchronizer that may be exclusively owned by a thread.
 * <p>它仅仅保证了一个同步器只能同时被一个线程拥有:</p>
 * <ul>
 *     <li>同步器是什么？ 是一个队列的变体 FIFO Queue ? == 'Q'</li>
 *     <li>CLH有一个宏观的理解, 队列不过两点 入队, 出队, 这是一个组织的数据结构, 是内存模型的体现</li>
 *     <li>只能同时被一个线程拥有, 这个怎么理解, 拥有了这个同步器其实就要要获取CPU的执行权, 让多核处理器能够并发安全的对同内存变量进行计算 == 'S'</li>
 *     <li><strong>【设计思想 就是通过一个原子int 状态变量来保证同步, 自定义同步器  就是通过这个 同步器的 共享变量 来完成同步的控制, 同时按照自己的需求
 *     重写tryRelease() 和 tryAcquire()  说白了就是你通过监控操作 state 这个同步器的全局变量 来控制获取锁 和 释放锁，同时提供使用AQS的模板方法】
 *     </strong></li>
 *     <li>作者设计这个同步模型, 让用户自主继承这个模型, 然后自己定义 <b>改变状态</b> 的 <b>保护方法</b> == 'A'</li>
 *     <li>就是定义 acquire release, 你自己定义的子类可以拥有自己的状态变量, AQS其他方法都是 queue 和 block 的机制实现</li>
 * </ul>
 *
 *
 * <h2>AQS - Queue, CLH</h2>
 * <ul>
 *     <li>CLH的基本概念, 它是一个高性能的, 基于单链表的, 公平的 自旋锁 spinlocks</li>
 *     <li>当前线程不断轮训 前驱节点 state获取锁, 如果获取了就解锁, 让后继节点来轮训它</li>
 *     <li>AQS是使用了它的一个变体, 通过细节探究它究竟 变体在什么地方</li>
 *     <li>作者没有使用它做自旋锁, 而是 使用它 做  阻塞同步器, 同时保留它 能通过当前线程节点的前驱节点的状态来控制</li>
 *     <li>A node is signalled when its predecessor releases</li>
 * </ul>
 *
 * <h2>Node</h2>
 * <ol>
 *     <li>定义了状态的 常量：</li>
 *     <li><b>CANCELLED: 1</b> 这个节点的线程被 中断 或者 超时了取消了执行, 这个node的状态不会再发生变化, 它也不需要获取锁 执行</li>
 *     <li><b>SIGNAL: -1</b>
 *      <ul>
 *          <li>
 *              当前节点的waitStatus=SIGNAL, 说明它的后继节点需要被park掉；is blocked 或者 will soon be.
 *          </li>
 *          <li>
 *               如果当前节点释放掉或者cancel掉, 当前节点的后继节点需要被 UNPACKING, 唤醒后继节点；
 *          </li>
 *          <li>
 *              为了避免竞争, 获取方法必须 <b>第一时间表明</b> 它需要一个信号标识, 然后重试原子获取, 如果失败就阻塞。
 *          </li>
 *      </ul>

 *     </li>
 *     <li><b>CONDITION: -2</b> 表明这个节点在 condition queue.</li>
 *     <li><b>PROPAGATE: -3</b> releaseShared should be propagated to other nodes. 这个是能在 doReleaseShared 保证传播继续
 *      其他操作已经介入干扰了。
 *     </li>
 * </ol>
 *
 * @author: jinyun
 * @date: 2021/3/19
 */
public class AbstractQueuedSynchronizerTest implements QueueOperation, ExportedMethodImpl, AcquireOperation {

    // Queue basic operations：

    /**
     * private Node enq(final Node node): 保证并发原子的入队操作, 懒加载进行头部初始化。
     */
    @Test
    @Override
    public void enqTest() {
        // 1. 获取当前队列的尾节点 tail;
        // 2. 如果没有尾结点，说明这个CLH Queue还没有初始化;
        // 3. 初始化一个头结点Head, 将尾结点也指向这个节点，自旋如果已经被其他线程抢占执行了就不设置头节点;
        // 4. 再将这个节点的前驱节点设置成尾节点，自旋操作抢占一直获取最新的尾节点然后把这个节点的后继节点设置成这个节点;
        // 5. 然后把这个节点的前驱节点(通过CAS安全的更新的)的后继节点设置成当前节点 返货这个节点的前驱节点;
        // 6. node.prev = t(tail) 这个就是直接把传入的这个节点的前驱直接指向tail的地址, 然后tail和node自旋设置;
        // 7. 永远都是返回最新的尾结点的前驱节点;

        // 1. 如果没有并发就只有当前这个一个线程封装的node0,
        // 只有一种情况： head = node0, tail = node0, node0<->node0<->node0, 返回一个node0.
        // 2. 如果出现并发就会出现抢占head, 抢占tail,
        // 2.1> head=node0, tail=node0 然后出现了线程1抢占了执行权, 线程0等待OS调度
        // 2.2> node0<->node1, head=node0, tail=node1,  线程1返回 node0
        // 2.3> 线程0重新执行, node0<->node1<->node0, 线程0返货 node1
    }

    /**
     * private Node addWaiter(Node mode): 简便方法, 使用 排他/共享 模式把当前线程封装成Node 入队。
     */
    @Test
    @Override
    public void addWaiterTest() {
        // 1. 先直接node = new Node(currentThread, mode), mode参数传入;
        // 2. 直接enq(node)就结束了, 但是这里补充了  尝试快速的入队操作, 失败了使用安全的入队方式;
        // 2.1> 获取当前队列的尾结点 tail = node0; 如果尾节点存在, head一定存在
        // 2.2> 如果tail存在, 尝试一次, node0<->node, tail=node;
        // 3. 返回封装的node节点
    }

    /**
     * private void setHead(Node node): 设置CLH的头节点, 出队操作
     * 只能被 acquire() 这个方法调用setHead(node)
     */
    @Test
    @Override
    public void setHeadTest() {
        // 1. head = node, 直接把head更新成node
        // 2. 这个node的thread=null,prev=null, 线程和前驱节点都置为null, 后面具体看 acquire() 这个设计的意图
    }

    /**
     * private void unparkSuccessor(Node node): 唤醒这个节点的后继节点, 如果后继节点存在
     * 独占锁唤醒 这个队列的后继节点
     */
    @Test
    @Override
    public void unparkSuccessorTest() {
        // 1. 获取node.waitStatus < 0 代表的状态 Signal, condition, propagate这三种状态
        // 2. 把当前节点的waitStatus 原子操作置为0
        // 3. 开始唤醒这个节点的后继节点了
        // 3.1> 获取这个节点的后继节点 successor;
        // 3.2> node->successor0->node->tail 如果后继节点null或者cancelled的, 就从这个队列的tail往前面找一个node s
        // 3.3> 最后这个node s如果不为空, 就使用原子操作, unpark(s.thread)
    }


    /**
     * private void doReleaseShared(): 释放操作对于共享模式
     * 共享锁唤醒 这个队列的后继节点
     */
    @Test
    @Override
    public void doReleaseSharedTest() {
        // 1. 这个释放工作, 需要保证头存在 并且不能和tail相同(因为要唤醒head节点的下一节点), 整个释放工作, 队列的头不能发生变化
        // 2. head的 waitStatus == Signal(-1), 然后使用原子操作 把 waitStatus 设置成 0
        // 使用就会重新获取最新的头 执行同样的操作, head's waitStatus = signal, 把状态置为0 唤醒head的后继节点
        // 3. 如果waitStatus=0 并且 成功把头部节点设置 Propagate(-3) 然后走下面判断头 完成操作。
    }

    /**
     * private void setHeadAndPropagate(Node node, int propagate): 设置头节点和传播状态
     * 设置队列的头节点, 并且检查后继节点是否处于共享模式的等待
     * 如果要么 propagate > 0 要么 propagate 状态被设置了。
     */
    @Test
    @Override
    public void setHeadAndPropagateTest() {
        // 1. 先使用了局部变量记录下原来的head, 把这个节点设置成头节点
        // 2. todo: 其他地方为啥调用 就是 waitStatus 这4个状态的实际业务意义
    }


    // Utilities for various versions of acquire


    /**
     * private void cancelAcquire(Node node): 取消正在尝试获取Node
     * ongoing: 持续进行的。
     * 这个方法就是把 指定node 从 队列里直接干掉, 同时检查这个队列中 这个移除节点到 头结点之间已经取消的线程。
     */
    @Test
    @Override
    public void cancelAcquireTest() {
        // 当前节点为null 直接结束, 什么情况会让这个 node == null ?
        // 把当前节点的线程置为null, 就是把这个节点的线程取消掉获取执行权的能力
        // ==> 这样线程的引用直接置为null, 就失去了这个线程引用, 但是其他引用还存在, 这个线程一直被挂起?

        // 从这个节点开始一直往head开始查找一个 非取消状态的 pred
        // 把当前节点的 waitStatus 置为 1 cancelled.

        // 如果当前节点是tail, 我们把非取消的节点置为tail, 同时把新的tail的后置节点置null
        // 如果不是tail, 如果它的后继节点需要 标记, 应该把后继节点拼接到pred.next
        // 具体的情况判断：
        // 1. 找到的pred不是头节点
        // 2. (pred的 waitStatus是 signal 或者 (ws<0 并且 能把这个pred的节点的waitStatus改成 signal) )
        //     这里为什么要把 pred 的waitStatus 改成 SIGNAL(说明它的后继节点的线程被pack).
        // 3. pred.thread 不为空
        // 上述条件3个都成立, 就把当前节点的后继节点挂到pred.next
        // 只要有一个不成立, 直接唤醒当前节点的后继节点, 后继节点就可以执行了. 这说明这个后继节点就是 头节点的后继节点。
        // node.next = node
    }

    /**
     * private static boolean shouldParkAfterFailedAcquire(Node pred, Node node):
     * <p>私有的静态方法</p>
     *
     *
     * 根据前置节点和当前节点, 应该把线程挂起在多次失败获取之后。
     * 主要就是优化 让出CPU的执行权, 避免空轮训, 避免CPU资源的浪费
     */
    @Test
    @Override
    public void shouldPackAfterFailedAcquireTest() {
        // 1. 如果前驱节点的 waitStatus == signal（-1） 直接返回 true, 就是状态量定义的表述 就是 后继节点node 需要被马上立刻被park
        // This node has already set status  asking a release to signal it, so it can safely park.
        // 2. 找前驱节点, 把找到了一个没有cancelled的node的后继节点指向 当前节点
        // 3. 如果前驱节点的 状态是0 -3, 把前驱节点的 状态设置为signal, 但是不会立即park当前节点的线程
        // 4. 调用方应该重试来确认 它不能获取 在 parking 之前。
    }


    /**
     * final boolean acquireQueued(Node node, arg):
     * 已经入队的node, 获取 排他的 非中断 执行权。
     * Used by condition wait methods as well as acquire.
     */
    @Test
    @Override
    public void acquireQueuedTest() {
        // 队列里已经入队的节点 默认获取是失败的
        // 默认也是指定节点的线程 没有被中断
        // 开始自旋, 获取当前节点的前驱节点
        // 如果前驱节点是 head: 然后调用排他的 tryAcquire(arg) 返回 true, 才能把当前节点设置成 head,
        // 原来的头节点回收, failed=false, 返回interrupted.
        // 如果前驱节点不是 head: shouldPackAfterFailedAcquire() 然后这个让调用方线程直接park()
    }

    /**
     * 获取 在 排他模式 中断模式。
     */
    @Test
    @Override
    public void doAcquireInterruptiblyTest() {
        // addWaiter(exclusive) 把当前线程和模型封装成node 入队 返回node
        // 自旋：
        // 和 acquireQueued 相识, 就是如果这个interrupted() 会直接抛出异常。
    }


    /**
     * 自旋获取锁 在规定的时间内完成操作
     */
    @Test
    @Override
    public void doAcquireNanosTest() {

    }


    // exported method. Should implemented by subclass (private inner help class)

    /**
     * 线程尝试获取锁(同步器),在排他模式下。
     * 如果这个方法失败了，那个应该把这个线程入队，就是如果它还没有入队的话
     * 知道它接收到了其他线程发出来 release 的 signal, 简述 它被标记。
     * 这个方法可以被用于实现 Lock.tryLock() 方法。
     * 这个方法的实现 必须检查 state 的状态, 只有这个状态 允许的情况下才能获取。
     */
    @Test
    @Override
    public void tryAcquireTest() {

    }

    /**
     * 尝试性的 设置 AQS全局state 状态量 来反映一个释放操作，在排他模式下。
     * 在线程释放的时候调用。
     */
    @Test
    @Override
    public void tryReleaseTest() {

    }


    /**
     * 共享锁模式下尝试获取同步器, 这个方法的实现 必须检查 state 的状态, 只有这个状态 允许的情况下才能获取。
     * 除了模式不一样, 其他的规范和排他模式下是一样的。
     *
     * 返回值是 int r
     * r < 0: 失败, failure
     * r = 0: acquisition in shared mode succeeded but no subsequent shared-mode acquire can succeed.
     * r > 0: 同样获取成功, 后面获取德也或许可以成功, 原因在于 后续的线程一定要检查 availability.
     */
    @Test
    @Override
    public void tryAcquireSharedTest() {

    }


    /**
     * 尝试性的 设置 AQS全局state 状态量 来反映一个释放操作，在共享模式下。
     * 在线程释放的时候调用。
     */
    @Test
    @Override
    public void tryReleaseSharedTest() {

    }

    /**
     * 返回当前线程的同步器 是不是排他模式
     * ConditionObject
     */
    @Test
    @Override
    public void isHeldExclusivelyTest() {

    }


    /**
     * 中断当前的线程
     * Thread.currentThread().interrupt();
     */
    @Test
    public void selfInterruptTest() {
        Thread thread = new Thread(() -> {
            System.out.println("Thread.currentThread().interrupt()");
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                System.out.println("我被JVM其他线程中断了, JVM检测到了中断标识位, 这个线程出现已经准备继续还是终止");
            }
        });

        thread.start();
        thread.interrupt(); // 改变中断标识位
        System.out.println(thread.isInterrupted()); // isInterrupted(false) 不会擦除印记
        Sleeper.sleep(6);
    }

    /**
     * 阻塞当前线程 返回当前线程的中断标识位
     * // isInterrupted(true) 擦除印记 :  Thread.interrupted(); 静态擦除
     */
    @Test
    public void parkAndCheckInterruptTest() {
        // 返回调用线程的中断状态
        LockSupport.park(this);
        Thread.interrupted();
    }


    // 这里就是一个简单的双向列表, 还是单线程下进行操作

    /**
     * 没有并发操作的。
     */
    @Test
    public void nodeTest() {
        Node current = new Node(1);
        // 前驱
        Node predecessor = new Node(0);
        // 后继
        Node successor = new Node(2);
        // 使用双向链表
        current.next = successor;
        current.prev = current;
        predecessor.next = current;
        successor.prev = current;
    }

    /**
     * node
     * 提供了几个构造方法,
     * 这几个构造方法用于各种情况
     */
    static class Node {
        /**
         * lazily initialized:
         */
        Node prev;
        Node next;
        final int val;

        public Node(int val) {
            this.val = val;
        }
    }
}
