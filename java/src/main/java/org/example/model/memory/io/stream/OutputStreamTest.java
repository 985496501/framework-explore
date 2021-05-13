package org.example.model.memory.io.stream;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 这个 输出流的 测试方法，提供各种子类的测试
 * <p>
 * {@link java.io.OutputStream}: 仅仅定义了类型, 要求子类必须实现 至少 write(byte) 这个方法,
 * 并且模板设计了 write(byte[]) 实际就是借助了write(byte)方法;
 *
 * <p>
 * <a href="https://www.processon.com/diagraming/5d32e637e4b043dcf83f65de">JAVA IO 的设计</a>
 *
 * @author: jinyun
 * @date: 2021/5/13
 */
public class OutputStreamTest {


    /**
     * 妈的这个类就是把数据全部在这个对象中缓冲进去
     */
    @Test
    public void byteArrayOutputStreamTest() {
        byte[] bytes = "hello world".getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bytes.length);
        // 循环赋值, 性能较低
//        byteArrayOutputStream.write(bytes);
        // call system.copyBytes()
        byteArrayOutputStream.write(bytes, 0, bytes.length);

        System.out.println(new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8));
    }

}
