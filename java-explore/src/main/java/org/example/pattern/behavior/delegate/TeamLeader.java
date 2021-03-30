package org.example.pattern.behavior.delegate;

/**
 * @author: jinyun
 * @date: 2021/2/22
 */
public class TeamLeader implements ITask {
    @Override
    public void doTask() {
        System.out.println("我是团队成员中的组长，我就负责划水");
    }
}
