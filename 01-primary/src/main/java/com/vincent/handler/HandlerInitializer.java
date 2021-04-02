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
        /*
        * HttpServerCodec是什么？是HttpRequestDecoder, HttpResponseEncoder的复合体
        * HttpRequestDecoder：http请求解码器，将channel中的bytebuf数据解码为httpResquest对象
        * HttpResponseEncoder：http响应解码器，将HTTPResponse对象编码为将要在channel中发送的bytebuf数据
        * */
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
        pipeline.addLast(new HttpServerCodec());
//        将自定义的处理器放入到pipeline的最后
        pipeline.addLast(new CostumerServerHandler());
    }
}
