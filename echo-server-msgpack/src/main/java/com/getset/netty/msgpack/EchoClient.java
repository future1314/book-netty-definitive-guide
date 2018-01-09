package com.getset.netty.msgpack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class EchoClient {
    public static void main(String[] args) {
        new EchoClient().connect("localhost", 8080);
    }

    public void connect(String host, int port) {
        Bootstrap client = new Bootstrap();
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        client.group(clientGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("frame decoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        ch.pipeline().addLast("msgpack decoder", new MsgPackDecoder());
                        ch.pipeline().addLast("frame encoder", new LengthFieldPrepender(2));
                        ch.pipeline().addLast("msgpack encoder", new MsgPackEncoder());
                        ch.pipeline().addLast(new EchoClientHandler(100));
                    }
                });
        try {
            ChannelFuture channelFuture = client.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
        }
    }
}
