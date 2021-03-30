package org.example.pattern.behavior.delegate;

/**
 * @author: jinyun
 * @date: 2021/2/22
 */
public class Member implements ITask {
    @Override
    public void doTask() {
        System.out.println("我是组员， 我负责淦最基础的活， 比如最脏 最累的活");
    }
}
