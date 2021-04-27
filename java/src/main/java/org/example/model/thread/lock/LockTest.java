package org.example.model.thread.lock;

/**
 * {@link java.util.concurrent.locks.Lock} 1.5 jdk提供的接口
 * lock的实现类提供了更多扩展的锁方法 可以用于同步方法或者语句;
 * 使用非常灵活, 支持多种相关的 {@link java.util.concurrent.locks.Condition}
 *
 * lock是控制多线程访问共享资源的工具, 临界区, 临界资源;
 * 通常, 锁提供了独占获取临界资源: 仅仅允许一个线程在同一时刻可以获取锁 才能获取共享资源(被这把锁保护的)
 * 但是, 一些锁允许并发获取共享资源, 比如读锁, {@link java.util.concurrent.locks.ReadWriteLock}
 *
 * Lock实现提供额外的功能, 提供非阻塞方法 tryLock() 提供超时机制 tryLock(long, unit)
 * 提供可以中断的锁 lockInterruptibly()
 *
 * Lock 类提供与 监视器锁 不同的行为和语义, 比如保证顺序性, 不可重入, 死锁检测;
 * 如果实现的子类 实现了 这些语义, 必须写明 这些语义的 文档;
 *
 * Lock的实例就是一个普通的对象, 他们也能用于 sync 语句中, 他们之间没有任何关系;
 *
 *
 * 所有的锁实现类 必须强制 保证内存同步语义 就像内置的builtin 监视器锁一样;
 *
 *
 *
 * @author: jinyun
 * @date: 2021/4/26
 */
public class LockTest {
    





}
