package org.example.thread.executor.aqs;

/**
 * @author: jinyun
 * @date: 2021/3/31
 */
public interface AcquireOperation {
    void cancelAcquireTest();

    void shouldPackAfterFailedAcquireTest();

    void acquireQueuedTest();

    void doAcquireInterruptiblyTest();

    void doAcquireNanosTest();

    void doAcquireSharedTest();
}
