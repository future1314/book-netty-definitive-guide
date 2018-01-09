package com.getset.netty;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class User implements Serializable {
    private int userId;
    private String userName;

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] codeC() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.putInt(userId);
        buffer.putInt(userName.length());
        buffer.put(userName.getBytes());
        buffer.flip();
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }

    public byte[] codeC(ByteBuffer buffer) {
        buffer.clear();
        buffer.putInt(userId);
        buffer.putInt(userName.length());
        buffer.put(userName.getBytes());
        buffer.flip();
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
