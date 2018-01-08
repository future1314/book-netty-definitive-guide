package com.getset.netty.nio2;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ClientReadCompletionHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {
    private AsynchronousSocketChannel socketChannel;

    public ClientReadCompletionHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer result, AsynchronousSocketChannel attachment) {

    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

    }
}
