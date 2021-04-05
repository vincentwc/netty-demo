package com.vincent.rpc.register;

/**
 * 注册规范
 */
public interface RegisterCenter {

    /**
     * @param serviceName    业务接口
     * @param serviceAddress ip:端口:实现类名
     */
    void register(String serviceName, String serviceAddress) throws Exception;
}
