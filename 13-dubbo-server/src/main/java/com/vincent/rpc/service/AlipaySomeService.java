package com.vincent.rpc.service;

import com.vincent.service.SomeService;

public class AlipaySomeService implements SomeService {
    @Override
    public String hello(String name) {
        return name + "欢迎你！--- AlipaySomeService";
    }
}
