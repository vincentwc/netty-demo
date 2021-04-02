package com.vincent.server;

import com.vincent.handler.CostumerServerHandler;
import com.vincent.handler.HandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务器启动类
 */
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
                    .childHandler(new HandlerInitializer());
            /*
              指定当前服务器所监听的端口号
              bind()方法的执行是异步的
              sync()方法会使bind()操作与后续的代码的执行有异步变为了同步
             */
            ChannelFuture future = bootstrap.bind(8888).sync();

            System.out.println("服务器启动成功，监听端口号为：8888");
            /*
              关闭channel
              closeFuture()的执行是异步的
              sync()closeFuture()操作与后续的代码的执行有异步变为了同步，
              当channel调用了close()方法并关闭成功后才会触发closeFuture()方法的执行
             */
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
