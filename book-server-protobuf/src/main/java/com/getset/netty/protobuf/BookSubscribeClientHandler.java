package com.getset.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class BookSubscribeClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SubscribeReqProto.SubscribeReq subscribeReq =
                SubscribeReqProto.SubscribeReq.newBuilder()
                .setSubReqId(1)
                .setUserName("Zhangsan")
                .setProductName("Netty book")
                .setAddress("Tieling")
                .build();
        ctx.writeAndFlush(subscribeReq);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Server has accepted your request : " + msg);
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
