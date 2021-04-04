package com.vincent.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerStrap {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
//                            添加一个行解码器
                            pipeline.addLast(new LineBasedFrameDecoder(2048));
                            /**
                             * 空闲状态处理器
                             * int readerIdleTimeSeconds,  读状态空闲时间
                             * int writerIdleTimeSeconds,  写状态空闲时间
                             * int allIdleTimeSeconds      读和写状态空闲时间
                             * 若3秒内没有触发读操作，会触发读操作空闲事件
                             * 若5秒内没有触发读操作，会触发写操作空闲事件
                             * 若7秒内读和写操作任意一项没有发生，都会触发all操作空闲事件
                             * Specify 0 to disable[0代表无效|禁用]
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7));
//                            StringDecoder 解码器
                            pipeline.addLast(new StringDecoder());
//                            StringEncoder 编码器
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new ServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(8888).sync();
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
