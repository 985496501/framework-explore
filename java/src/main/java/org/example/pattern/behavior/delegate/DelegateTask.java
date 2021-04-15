package org.example.pattern.behavior.delegate;

import java.util.LinkedList;
import java.util.List;

/**
 * 委派模式  这东西不是属于 23种设计模式 和下面的很相似 傻傻分不清楚 妈的不用分
 * <pre>
 *     1. 策略模式(similar):  注重结果
 *     2. 代理模式(diff what): 注重过程
 * <pre/>
 * 他妈就是写法的问题  构建处理问题的一般死写法
 * 完全按需随意写  你想怎么写就怎么写 完全取决于你对问题的划分力度。
 *
 *
 * 委派模式：
 * 屏蔽了具体的实现者  关注的是委派人
 * 妈的对外来看 就是委派人搞得一样 实际是别人搞得 前人种树后人乘凉。
 * <pre>
 * 不关心过程，只关心结果
 * 调用方的要求是 我不问你怎么搞得  我就要的东西 所以我才委派你
 * 一个任务过来
 * 我委派了你
 * 我不问你怎么搞(具体调用了多少个对象的方法我不问) 必须给我一个结果
 * <pre/>
 *
 * 代理模式区别：
 * 关心的过程   不关心结果
 *
 *
 * @author: jinyun
 * @date: 2021/2/22
 */
public class DelegateTask implements ITask {

    private final List<ITask> workers = new LinkedList<>();

    public List<ITask> getWorkers() {
        return workers;
    }

    public void setWorker(ITask iTask) {
        workers.add(iTask);
    }

    @Override
    public void doTask() {
        // nothing to do
        System.out.println("我什么活都不干  我负责把任务分配干活的人 你们赶快给我淦活...");
        workers.forEach(ITask::doTask);
    }

    public static void main(String[] args) {
        DelegateTask delegateTask = new DelegateTask();
        delegateTask.setWorker(new TeamLeader());
        delegateTask.setWorker(new Member());
        delegateTask.doTask();
    }
}
