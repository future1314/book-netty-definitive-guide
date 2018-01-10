package com.getset.netty.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProtoTest {
    @Test
    public void testSubscribeReqProto() throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        byte[] bytes = req.toByteArray();
        SubscribeReqProto.SubscribeReq subscribeReq = SubscribeReqProto.SubscribeReq.parseFrom(bytes);
        System.out.println(subscribeReq);
        assertEquals(req, subscribeReq);
    }

    private SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqId(1);
        builder.setUserName("Zhangsan");
        builder.setProductName("Netty book");
        builder.setAddress("Tieling");

        return builder.build();
    }
}
