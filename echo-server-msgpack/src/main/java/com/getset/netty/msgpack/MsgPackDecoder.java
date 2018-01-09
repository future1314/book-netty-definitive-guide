package com.getset.netty.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final int length = in.readableBytes();
        MessagePack messagePack = new MessagePack();
        byte[] raw = new byte[length];
        in.getBytes(in.readerIndex(), raw, 0, length);
        out.add(messagePack.read(raw));
    }
}
