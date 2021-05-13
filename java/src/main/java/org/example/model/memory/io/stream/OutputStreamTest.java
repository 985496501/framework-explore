package org.example.model.memory.io.stream;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

        // 需要一个具备写入物理设备的 输出流进行真正的写出 FileOutputStream 它会系统调用文件系统的 open() 方法;
        try (FileOutputStream fileOutputStream = new FileOutputStream("E:\\learning\\framework\\java\\src\\main\\java\\org\\example\\model\\memory\\io\\stream\\temp\\1.txt", true)) {
            byteArrayOutputStream.writeTo(fileOutputStream);
        } catch (FileNotFoundException e) {
            System.out.println("file 没有找到...");
        } catch (IOException e) {
            System.out.println("发生了 未知 的IO 异常...");
        }
    }

}
