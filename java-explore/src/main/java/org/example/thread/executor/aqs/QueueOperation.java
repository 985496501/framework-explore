package org.example.thread.executor.aqs;

/**
 * @author: jinyun
 * @date: 2021/3/31
 */
public interface QueueOperation {
    void enqTest();

    void addWaiterTest();

    void setHeadTest();

    void unparkSuccessorTest();

    void doReleaseSharedTest();

    void setHeadAndPropagateTest();
}
