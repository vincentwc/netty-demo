package com.vincent.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerStrap {

    public static void main(String[] args) {
//        用于处理客户端连接请求,将请求发送给workerGroup中的eventloop
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

//        用于启动serverChannel
            ServerBootstrap bootstrap = new ServerBootstrap();
//        指定eventLoopGroup
            bootstrap.group(bossGroup, workerGroup)
//                指定使用NIO进行通信
                    .channel(NioServerSocketChannel.class)
//                指定处理器 ---》 对应的是bossGroup
//                .handler()
//                指定处理器 ---》 对应的是workerGroup中的eventLoop所绑定的线程所要处理的处理器
                    .childHandler(null);

            ChannelFuture future = bootstrap.bind(8888).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
