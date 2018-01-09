package com.getset.netty;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class UserTest {
    /**
     * 测试发现Java对象序列化后的字节数比实际需要传递的信息量要大
     */
    @Test
    public void testSerializationSize() throws IOException {
        User user = new User(123, "Zhangsan");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(user);
        byte[] bytesFromSerialization = baos.toByteArray();
        System.out.println("Length of serialization: " + bytesFromSerialization.length);
        System.out.println("Length of codeC: " + user.codeC().length);
    }

    /**
     * 测试发现Java对象序列化的速度相对要慢
     */
    @Test
    public void testSerializationPerf() throws IOException {
        User user = new User(123, "Zhangsan");
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(user);
            oos.flush();
            oos.close();
            baos.toByteArray();
            baos.close();
        }
        System.out.println("Java Serialization took " + (System.currentTimeMillis() - start) + " milliseconds.");
        start = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        for (int i = 0; i < 100000; i++) {
            user.codeC(buffer);
        }
        System.out.println("Customized Serialization took " + (System.currentTimeMillis() - start) + " milliseconds.");
    }
}
