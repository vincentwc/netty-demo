package com.vincent.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 管道初始化器
 */
public class HandlerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 当channel初始创建完毕后会触发该方法的执行，用于初始化channel
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        从channel获取pipeline
        ChannelPipeline pipeline = ch.pipeline();
//        将HttpServerCodec处理器放入到pipeline的最后
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
        pipeline.addLast(new HttpServerCodec());
//        将自定义的处理器放入到pipeline的最后
        pipeline.addLast(null);
    }
}
