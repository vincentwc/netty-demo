package com.vincent.rpc.consumer;

import com.vincent.service.SomeService;

public class RpcConsumer {

    public static void main(String[] args) {
        SomeService someService = RpcProxy.create(SomeService.class);
        System.out.println(someService.hello("开课吧"));
        System.out.println(someService.hashCode());
    }

}
