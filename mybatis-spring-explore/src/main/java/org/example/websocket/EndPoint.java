package org.example.websocket;

/**
 * @author: jinyun
 * @date: 2021/4/7
 */
public interface EndPoint {
    void pushToOne(Long userId, String eventName, Object data);
}
