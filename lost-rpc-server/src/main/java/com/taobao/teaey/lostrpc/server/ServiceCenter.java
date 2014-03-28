package com.taobao.teaey.lostrpc.server;

/**
 * @author xiaofei.wxf
 */
public interface ServiceCenter {
    boolean add(Object service);

    Object get(String serviceName);
}
