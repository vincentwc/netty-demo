package com.vincent;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * channelRead不会释放接收到来自于对方的msg
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + ", " + msg);
        ctx.channel().writeAndFlush("from server : " + UUID.randomUUID());
        TimeUnit.MILLISECONDS.sleep(500);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
