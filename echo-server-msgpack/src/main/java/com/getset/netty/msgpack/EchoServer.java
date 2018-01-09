package com.getset.netty.msgpack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class EchoServer {
    public static void main(String[] args) {
        new EchoServer().bind(8080);
    }

    public void bind(int port) {

        ServerBootstrap server = new ServerBootstrap();
        EventLoopGroup dispatchers = new NioEventLoopGroup();
        EventLoopGroup workers = new NioEventLoopGroup();
        server.group(dispatchers, workers)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("frame decoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        ch.pipeline().addLast("msgpack decoder", new MsgPackDecoder());
                        ch.pipeline().addLast("frame encoder", new LengthFieldPrepender(2));
                        ch.pipeline().addLast("msgpack encoder", new MsgPackEncoder());
                        ch.pipeline().addLast(new EchoServerHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = server.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            dispatchers.shutdownGracefully();
            workers.shutdownGracefully();
        }
    }
}
