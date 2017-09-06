package com.shengsiyuan.protobufdemo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;


/**
 * Created by ququ1 on 2017/8/23.
 */
public class MyProtobufInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(new MyProtobufHanlder());
    }
}
