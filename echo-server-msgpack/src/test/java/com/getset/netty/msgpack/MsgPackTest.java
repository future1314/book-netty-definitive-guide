package com.getset.netty.msgpack;

import org.junit.Test;
import org.msgpack.MessagePack;

import java.io.IOException;

public class MsgPackTest {
    @Test
    public void testMsgPack() throws IOException {
        User user = new User(123, "zhangsan");
        MessagePack messagePack = new MessagePack();
        byte[] raw = messagePack.write(user);
        messagePack.register(User.class);
        User read = messagePack.read(raw, User.class);
        System.out.println(read);
    }
}
