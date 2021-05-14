package org.example.common;

/**
 * 整个流程管道中的一道处理器, 用于单一功能处理
 *
 * @author: jinyun
 * @date: 2021/5/14
 */
public interface IHandler {
    /**
     * 处理业务
     *
     * @param business 提交的业务
     */
    void handle(Business business);
}
