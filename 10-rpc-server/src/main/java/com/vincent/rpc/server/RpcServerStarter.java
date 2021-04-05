package com.vincent.rpc.server;

public class RpcServerStarter {
    public static void main(String[] args) throws Exception {
        RpcServer server = new RpcServer();
        // 发布服务提供者
        server.publish("com.vincent.rpc.service");
        // 启动server
        server.start();
    }
}
