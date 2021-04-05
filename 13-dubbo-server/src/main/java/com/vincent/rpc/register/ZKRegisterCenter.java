package com.vincent.rpc.register;

import com.vincent.constant.ZKConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;

public class ZKRegisterCenter implements RegisterCenter {

    private final CuratorFramework curator;

    public ZKRegisterCenter() {
        curator = CuratorFrameworkFactory.builder()
//                指定要连接的zk集群
                .connectString(ZKConstant.ZK_CLUSTER)
//                指定连接超时时限
                .connectionTimeoutMs(10000)
//                指定会话超时时限
                .sessionTimeoutMs(4000)
//                指定重试机制：每重试一次，休眠1秒，最多重试10次
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .build();
//        启动zk客户端
        curator.start();
    }

    @Override
    public void register(String serviceName, String serviceAddress) throws Exception {
//        创建持久节点
//        要创建的服务名称的节点名称
        String servicePath = ZKConstant.ZK_DUBBO_ROOT_PATH + "/" + serviceName;

        if (curator.checkExists().forPath(servicePath) == null) {
            curator.create()
                    //                如果父节点不存在则创建，存在则不创建
                    .creatingParentsIfNeeded()
                    //                指定创建持久节点
                    .withMode(CreateMode.PERSISTENT)
                    //                指定要创建的节点
                    .forPath(servicePath,"000".getBytes());
        }
//        创建临时节点
        String providerPath = servicePath + "/" + serviceAddress;
        if (curator.checkExists().forPath(providerPath) == null) {
            String path = curator.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(providerPath,"111".getBytes());
            System.out.println(path);
        }


    }
}
