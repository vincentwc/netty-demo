package com.vincent.rpc.server;

import com.vincent.dto.Invocation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

public class RpcServerHandler extends SimpleChannelInboundHandler<Invocation> {

    /*
     * 声明服务注册表
     * */
    private Map<String, Object> registerMap;

    public RpcServerHandler(Map<String, Object> registerMap) {
        this.registerMap = registerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation msg) throws Exception {
        Object result = "没有要访问的提供者";
//        判断注册表中是否存在指定服务
        if (registerMap.containsKey(msg.getImplementsClassName())) {
//            获取到相应的提供者实例，然后调用其相应方法
            Object provider = registerMap.get(msg.getImplementsClassName());
            result = provider.getClass().getMethod(msg.getMethodName(), msg.getParamTypes()).invoke(provider, msg.getParamValues());
            ctx.writeAndFlush(result);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
