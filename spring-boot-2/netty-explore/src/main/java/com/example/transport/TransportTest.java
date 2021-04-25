package com.example.transport;

import org.junit.Test;

/**
 * Transport: 传输层;
 * 传输层的目的 就是 建立连接, 有端的概念, Port, 可以进行端口服务, 一个端口16bit; 0-65535
 * 那么操作系统再定义这个端口的时候, 就是使用了两个字节;
 * 16个1      2^16 - 1 = 65535
 *
 * 一般我们都是以 <a href='https://www.cnblogs.com/ysuwangqiang/p/11485554.html'>TCP</a> 传输控制协议为主进行研究;
 *
 * 传输层
 *      上层 是 应用层：会话层, 表示层，应用层， 应用程序的协议
 *      下层 是 网络层： 作用就是ip寻址, ip路由, 找到源主机到目的主机的最合适的链路; 进行的是主机 与 主机 之间的通信;
 *
 *
 * @author: jinyun
 * @date: 2021/4/25
 */
public class TransportTest {
    @Test
    public void portTest() {
        System.out.println(Math.pow(2, 16) - 1);
    }
}
