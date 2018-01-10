package com.getset.netty.protobuf;

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
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class BookSubscribeClient {
    public static void main(String[] args) {
        new BookSubscribeClient().connect("localhost", 8080);
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
                        // ProtobufVarint32FrameDecoder 主要用于半包处理
                        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        // 解码器，参数是用来告诉解码器解码的目标类型
                        ch.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
                        // 半包处理
                        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        // 编码器
                        ch.pipeline().addLast(new ProtobufEncoder());
                        ch.pipeline().addLast(new BookSubscribeClientHandler());
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
