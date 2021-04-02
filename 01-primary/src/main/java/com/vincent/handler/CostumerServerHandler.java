package com.vincent.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 自定义服务端处理器
 * 需求：用户提交一个请求后，在浏览器上就会看到hello netty world
 */
public class CostumerServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当channel中有来自于客户端的数据时就会触发该方法的执行
     *
     * @param ctx 上下文对象
     * @param msg 就是来自于channel【客户端】中的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        msg instanceof 
//        System.out.println("msg = " + msg.getClass());
//        System.out.println("客户端地址 = " + ctx.channel().remoteAddress());
    }

    /**
     * 当channel中的数据在处理过程中出现异常时会触发该方法的执行
     *
     * @param ctx   上下文
     * @param cause 发生的异常对象
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
//        关闭channel
        ctx.close();
    }
}
