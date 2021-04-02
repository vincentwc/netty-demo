package com.vincent.server;

import com.vincent.handler.CostumerServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

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
//                    .childHandler(new HandlerInitializer());
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //        从channel获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
//        将HttpServerCodec处理器放入到pipeline的最后
                            /*
                             * HttpServerCodec是什么？是HttpRequestDecoder, HttpResponseEncoder的复合体
                             * HttpRequestDecoder：http请求解码器，将channel中的bytebuf数据解码为httpResquest对象
                             * HttpResponseEncoder：http响应解码器，将HTTPResponse对象编码为将要在channel中发送的bytebuf数据
                             * */
                            pipeline.addLast("HttpServerCodec", new HttpServerCodec());
                            pipeline.addLast(new HttpServerCodec());
//        将自定义的处理器放入到pipeline的最后
                            pipeline.addLast(new CostumerServerHandler());
                        }
                    });
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
