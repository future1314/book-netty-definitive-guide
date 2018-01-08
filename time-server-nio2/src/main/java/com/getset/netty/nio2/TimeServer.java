package com.getset.netty.nio2;

public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        new Thread(new AsyncTimeServerHandler(port)).start();
    }
}
