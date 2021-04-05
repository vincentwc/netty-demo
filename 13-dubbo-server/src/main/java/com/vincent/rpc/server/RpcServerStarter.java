package com.vincent.rpc.server;

import com.vincent.rpc.register.ZKRegisterCenter;

public class RpcServerStarter {
    public static void main(String[] args) throws Exception {
        RpcServer server = new RpcServer();
        // 发布服务提供者
        server.publish("com.vincent.rpc.service", new ZKRegisterCenter(), "127.0.0.1:8888");
        // 启动server
        server.start();
    }
}
