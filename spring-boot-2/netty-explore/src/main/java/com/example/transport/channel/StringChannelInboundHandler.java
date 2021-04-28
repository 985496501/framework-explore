package com.example.transport.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * This ChannelInboundHandlerAdapter which allows to explicit only handle a specific type of messages.
 * below is handle type of String;
 *
 * @author: jinyun
 * @date: 2021/4/28
 */
public class StringChannelInboundHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("----------------------->>> " + msg);
    }
}
