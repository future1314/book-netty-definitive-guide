package com.getset.netty.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AsyncTimeServerHandler implements Runnable {
    private AsynchronousServerSocketChannel serverSocketChannel;
    private CountDownLatch countDownLatch;

    AsyncTimeServerHandler(int port) {
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("Time server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AsynchronousServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        serverSocketChannel.accept(this, new AcceptCompletionHandler());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
