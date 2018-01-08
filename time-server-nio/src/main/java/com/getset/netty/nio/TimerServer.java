package com.getset.netty.nio;

public class TimerServer {
    public static void main(String[] args) throws InterruptedException {
        MultiplexerTimeServer server = new MultiplexerTimeServer(9090);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
