package com.example.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: jinyun
 * @date: 2021/5/26
 */
public class ByteArrayChannelInboundHandler extends SimpleChannelInboundHandler<byte[]> implements ChannelHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) {
        System.out.println(new String(msg));
    }



    // channelHandler的定义的 回调方法 telnet localhost 9632  ping 就会调用added, 断开连接就是removed.

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("我被包裹到 Context 中, 已经被加入到了 pipeline");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("我被包裹到 Context 中, 我被 pipeline 剔除了");
    }
}
