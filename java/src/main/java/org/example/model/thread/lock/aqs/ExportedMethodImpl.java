package org.example.model.thread.lock.aqs;

/**
 * @author: jinyun
 * @date: 2021/3/31
 */
public interface ExportedMethodImpl {
    void tryAcquireTest();

    void tryReleaseTest();

    void tryAcquireSharedTest();

    void tryReleaseSharedTest();

    void isHeldExclusivelyTest();
}
