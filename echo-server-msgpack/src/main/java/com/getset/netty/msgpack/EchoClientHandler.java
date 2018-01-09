package com.getset.netty.msgpack;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private final int count;

    public EchoClientHandler(int count) {
        this.count = count;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        User[] users = new User[count];
        for (int i = 0; i < count; i++) {
            users[i] = new User(100 + i, "user-" + i);
            ctx.write(users[i]);
        }
        ctx.flush();
//        ctx.writeAndFlush(new User(123, "zhangsan"));
//        ctx.writeAndFlush("test");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client get reply from server: " + msg);
//        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
