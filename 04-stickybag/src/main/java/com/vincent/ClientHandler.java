package com.vincent;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * <String>泛型为msg消息的类型一致
 * channelRead0自动释放掉msg的资源
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {
   private String message = "hello world";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + ", " + msg);
        ctx.channel().writeAndFlush("from client :" + LocalDateTime.now());
        TimeUnit.MILLISECONDS.sleep(500);
    }

    /**
     * 当channel被激活的时候会触发该方法执行
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.channel().writeAndFlush("from client：begin talking");
        byte[] bytes = message.getBytes();
        ByteBuf buffer = null;
        for (int i = 0; i < 2; i++) {
//            申请缓存空间
            buffer = Unpooled.buffer(bytes.length);
//            将数据写入缓存
            buffer.writeBytes(bytes);
//            将缓存数据写入到buffer
            ctx.writeAndFlush(buffer);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
