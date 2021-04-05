package com.vincent.rpc.balance;

import java.util.List;

/**
 * 负载均衡接口
 */
public interface LoadBalance {


    String choose(List<String> servers);
}
