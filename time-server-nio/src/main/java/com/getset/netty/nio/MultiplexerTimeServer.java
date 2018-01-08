package com.getset.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MultiplexerTimeServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel server;
    private volatile boolean stopped;

    MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(port), 1024);
            server.register(selector, SelectionKey.OP_ACCEPT);
            stopped = false;
            System.out.println("Timer server has started ...");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        while (!stopped) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keys = selectionKeys.iterator();
                SelectionKey key = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    keys.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        System.out.println("A client closed connection.");
                        if (key != null) {
                            key.channel();
                        }
                        if (key.channel() != null) {
                            key.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel serverSockerChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel;
                socketChannel = serverSockerChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(buffer);
                if (readBytes > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String request = new String(bytes, "UTF-8");
                    System.out.println("Time server received request: " + request);
                    String currentTime = "QUERY TIME ORDER".equals(request) ? new Date().toString() : "BAD ORDER";
                    buffer.clear();
                    buffer.put(currentTime.getBytes());
                    buffer.flip();
                    socketChannel.write(buffer);
                } else if (readBytes < 0) {
                    key.channel();
                    selector.close();
                    socketChannel.close();
                }
            }
        }
    }

    public void stop() {
        stopped = true;
    }
}
