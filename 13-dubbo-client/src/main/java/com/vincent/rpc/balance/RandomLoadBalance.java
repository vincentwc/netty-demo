package com.vincent.rpc.balance;

import java.util.List;
import java.util.Random;

/**
 * 随机数负载均衡
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public String choose(List<String> servers) {
        return servers.get(new Random().nextInt(servers.size()));
    }
}
