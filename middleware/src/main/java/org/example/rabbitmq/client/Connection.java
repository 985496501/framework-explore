package org.example.rabbitmq.client;

import java.io.Closeable;
import java.net.InetAddress;
import java.util.Map;

/**
 * To which connects a broker.
 * 首先抽象一个 连接, 这个抽象表示应用程序和 Broker 之间的连接
 * 那么这个连接需要那些功能：(容易定义接口)
 * 1. 连接能够自动释放吧 可以继承 {@link java.io.Closeable}
 * 2. 连接使端到端的, 应该由ip和端口号, ip {@link java.net.InetAddress} 端口号就是port
 * 3. 获取最大的channel数量  需与服务端协商
 * 4. 获取最大的帧的大小
 * 5. 需要通过这个连接维持 心跳吧 需要定义心跳的时间周期, 内部的;
 * 6. 还需要携带一些客户端属性 给服务端吧
 * 7. 给客户端分配一个名称 这个名称就是在 服务端
 * 8.
 *
 * @author: jinyun
 * @date: 2021/5/11
 */
public interface Connection extends Closeable {
    /**
     * Retrieve the host.
     *
     * @return the hostname of the peer to which we're connected.
     */
    InetAddress getAddress();

    /**
     * Retrieve the port.
     *
     * @return the port of the peer to which we're connected.
     */
    int getPort();

    /**
     * 获取协商的最大的channel数量, 可以使用的num区间再1到这个数字, 含于;
     * Get the negotiated maximum channel number. Usable channel numbers range
     * from 1 to this number, inclusive.
     *
     * @return the maximum channel number permitted for this connection.
     */
    int getMaxChannel();

    /**
     * 获取协商的最大帧的量, 单位8bit, 0代表没有限制
     * Get the negotiated maximum frame size.
     *
     * @return the maximum frame size, in octets; zero if unlimited.
     */
    int getMaxFrame();


    /**
     * Get the negotiated heartbeat interval.
     *
     * @return the heartbeat interval, in seconds; zero if none.
     */
    int getHeartbeat();

    /**
     * Get a copy of the map of client properties sent to the server.
     *
     * @return a copy of the map of client properties.
     */
    Map<String, Object> getClientProperties();

    /**
     * Returns client-provided connection name, if any.
     * Note that the value returned does not uniquely identify a connection
     * and cannot be used as a connection identifier in HTTP API requests.
     *
     * @return client-provided connection name, if any.
     */
    String getClientProvidedName();

    /**
     * Retrieve the server properties.
     *
     * @return a map of the server properties. This typically includes the product
     * name and version of the server.
     */
    Map<String, Object> getServerProperties();


}
