package com.vincent.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 创建一个channelgroup。是一个线程安全的集合，其中存放着与当前服务器相连接的所有active状态的channel
     * GlobalEventExecutor是一个单例，单线程的eventexecutor，是为了保证当前group中所有的channel的处理线程是同一个线程
     */
    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    /**
     * channelRead不会释放接收到来自于对方的msg
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        获取向服务器发送消息的channel
        Channel channel = ctx.channel();
//        将消息广播给所有group中的客户端channel
//        发送给自己的消息与发送给大家的消息是不一样的
        group.forEach(ch -> {
            if (ch != channel) {
                ch.writeAndFlush(channel.remoteAddress() + ": " + msg + "\n");
            } else {
                ch.writeAndFlush("me: " + msg + "\n");
            }
        });
    }

    /**
     * 只要有客户端channel与服务端连接成功，就会执行这个方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        获取当前与服务器连接成功的channel
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "------------上线");
        group.writeAndFlush(channel.remoteAddress() + "------------上线\n");
        group.add(channel);
    }

    /**
     * 只要有客户端断开与服务端的连接，就会执行这个方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        获取当前要断开连接的channel
        Channel channel = ctx.channel();
//        下一行代码不需要。channel钝化掉。group中存放的都是active状态的channel。一旦某channel的状态不再是active，group会自动将其从集合中剔除
//        remove
        /**
         * 下一行代码不需要。channel钝化掉。group中存放的都是active状态的channel。一旦某channel的状态不再是active，group会自动将其从集合中剔除
         * remove方法的场景是讲一个active的channel剔除group
         */
//        group.remove(channel);
        System.out.println(channel.remoteAddress() + "------------下线");
        group.writeAndFlush(channel.remoteAddress() + "------------下线，当前在线人数：" + group.size() + "\n");

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
