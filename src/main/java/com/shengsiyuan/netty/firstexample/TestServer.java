package com.shengsiyuan.netty.firstexample;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务端
 */
public class TestServer {

    public static void main(String[] args) throws Exception {
        //定义两个事件循环组, bossGroup用于接收连接, 不对连接进行处理, 转给workerGroup进行处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap(); //ServerBootstrap用于启动服务端
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new TestServerInitializer()); //TestServerInitializer是我们自己编写的请求初始化器

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync(); //绑定端口8899

            channelFuture.channel().closeFuture().sync(); //关闭监听
        } finally {
            //终止事件循环
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
