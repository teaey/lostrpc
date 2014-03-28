package com.taobao.teaey.lostrpc.server.reflect;

import com.taobao.teaey.lostrpc.server.ServiceCenter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xiaofei.wxf
 */
public class ReflectServiceCenter implements ServiceCenter {
    private ReflectServiceCenter() {
    }

    private static class Holder {
        static final ReflectServiceCenter i = new ReflectServiceCenter();
    }

    public static ReflectServiceCenter getInstance() {
        return Holder.i;
    }

    private final ConcurrentMap<String, Object> map = new ConcurrentHashMap<String, Object>();

    @Override
    public boolean add(Object service) {
        Class[] interfaces = service.getClass().getInterfaces();
        for (Class each : interfaces) {
            Service s = new Service(each.getName(), each, service);
            map.putIfAbsent(each.getName(), s);
        }
        return false;
    }

    @Override public Object get(String serviceName) {
        return map.get(serviceName);
    }
}
