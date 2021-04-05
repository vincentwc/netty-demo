package com.vincent.rpc.register;

public class ZKRegister {

    public static void main(String[] args) throws Exception {
        ZKRegisterCenter center = new ZKRegisterCenter();
        center.register("com.vincent.service.SomeService", "127.0.0.1:8888:com.vincent.service.SomeServiceImpl");
        System.in.read();
    }
}
