package com.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.junit.Test;

/**
 * @author: jinyun
 * @date: 2021/5/28
 */
public class LengthFieldBasedFrameDecoderTest {


    /**
     * 用于解决粘包的情况, 出现粘包的情况, 就是客户端开启了 nagle 算法:
     * 1. 发送的数据包如果达到了允许的最大报文长度, 这个是建立握手的时候, 双方发送option选型里面携带的信息, 会创建一个min(s,c)
     * MSS, maximum segment size;  tcp: 的数据包就是 segment.
     * 2. 并且接收了server的ack
     * 3. 200ms超时也会发送数据包.
     *
     * 也就是会 delay 发送数据包; 但是避免了网络拥塞
     *
     * 建立连接之后 server端会创建一个读写缓冲区, 读缓冲区, 传输层能保证发送的切片合成一个完成的数据包在 缓冲区, 这个缓冲区的字节肯定是tcp上层
     * 要求发送的完成包;
     *
     * 如果解决粘包  的问题;
     */
    @Test
    public void lengthFieldBasedFrameDecoderTest() {
        // 最大帧程度, 长度偏移量, 程度的步长, 长度调整, 默认去除的字节长度
        TestLengthFieldBasedFrameDecoder decoder = new TestLengthFieldBasedFrameDecoder(8, 0, 2, 0, 0);
//        decoder.decode();
    }

    public static class TestLengthFieldBasedFrameDecoder extends  LengthFieldBasedFrameDecoder {
        public TestLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
            super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        }

        @Override
        public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            return super.decode(ctx, in);
        }
    }
}
