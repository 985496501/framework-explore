package org.example.model.memory.io.zerocopy;


import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author: jinyun
 * @date: 2021/4/2
 */
public class ZeroCopyTest {
    @Test
    public void zeroCopyTest() {
        Path path = Paths.get(URI.create("file:///E:/test/1.txt"));
        try {
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE, /*StandardOpenOption.READ, */StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.APPEND);
            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
            String str = "hello world";
//            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
//            byteBuffer.put(bytes);
            fileChannel.write(ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void pathTest() {
        Path path = Paths.get(URI.create("file:///E:/test"));
    }
}
