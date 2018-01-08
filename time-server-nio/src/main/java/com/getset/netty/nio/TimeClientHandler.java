package com.getset.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TimeClientHandler implements Runnable {
    private SocketChannel socketChannel;
    private Selector selector;
    private InetSocketAddress socketAddress;
    private boolean stopped;

    public TimeClientHandler(String host, int port) {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketAddress = new InetSocketAddress(host, port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void doConnect() throws IOException {
        if (socketChannel.connect(socketAddress)) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    public void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] request = "QUERY TIME ORDER".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(request.length);
        buffer.put(request);
        buffer.flip();
        socketChannel.write(buffer);
        if (!buffer.hasRemaining()) {
            System.out.println("Time client has send request.");
        }
    }

    public void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isConnectable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                if (socketChannel.finishConnect()) {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    doWrite(socketChannel);
                } else {
                    System.exit(1);
                }
            }
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(buffer);
                if (readBytes > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    System.out.println("Now is " + new String(bytes, "UTF-8"));
                    stopped = true;
                } else if (readBytes < 0) {
                    key.channel();
                    socketChannel.close();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!stopped) {
            SelectionKey key = null;
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        System.out.println("Server closed connection.");
                        System.exit(1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
