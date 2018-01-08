package com.getset.netty.nio;

public class TimerClient {
    public static void main(String[] args) {
        new Thread(new TimeClientHandler("127.0.0.1", 9090)).start();
    }
}
