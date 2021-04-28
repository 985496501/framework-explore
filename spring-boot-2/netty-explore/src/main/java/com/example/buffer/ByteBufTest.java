package com.example.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * 1. creation of a buffer:
 * It is recommended to create a new buffer using the helper methods in Unpooled
 * rather than calling an individual implementation's constructor.
 *
 *
 * @author: jinyun
 * @date: 2021/4/28
 */
public class ByteBufTest {

    @Test
    public void byteBufTest() {
        ByteBuf byteBuf = Unpooled.directBuffer(16);
    }
}
