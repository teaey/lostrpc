package com.taobao.teaey.lostrpc.server.protobuf;

import com.google.protobuf.BlockingService;
import com.google.protobuf.Service;
import com.taobao.teaey.lostrpc.server.ServiceCenter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xiaofei.wxf on 14-2-13.
 */
public class ProtobufServiceCenter implements ServiceCenter {
    private final ConcurrentMap<String, BlockingService> BLOCKING_SERVICE_CACHE =
        new ConcurrentHashMap<String, BlockingService>(1 << 10);
    private final ConcurrentMap<String, Service> SERVICE_CACHE =
        new ConcurrentHashMap<String, Service>(1 << 10);

    private ProtobufServiceCenter() {
    }

    private static class Holder {
        static final ProtobufServiceCenter i = new ProtobufServiceCenter();
    }

    public static ProtobufServiceCenter theOne() {
        return Holder.i;
    }
    /*public void addService(Object service) {
        if (null == service) {
            throw new NullPointerException("service");
        }
        if (!(service instanceof Service) && !(service instanceof BlockingService)) {
            throw new IllegalArgumentException(service.getClass().getName());
        }
        if (service instanceof Service) {
            SERVICE_CACHE.put(((Service) service).getDescriptorForType().getFullName(), (Service) service);
        }
        if (service instanceof BlockingService) {
            BLOCKING_SERVICE_CACHE.put(((BlockingService) service).getDescriptorForType().getFullName(), (BlockingService) service);
        }
    }

    public static BlockingService getBlockingService(String fullname) {
        if (null == fullname) {
            throw new NullPointerException("fullname");
        }
        return BLOCKING_SERVICE_CACHE.get(fullname);
    }*/

    @Override public boolean add(Object service) {
        if (null == service) {
            throw new NullPointerException("service");
        }
        if (!(service instanceof Service) && !(service instanceof BlockingService)) {
            throw new IllegalArgumentException(service.getClass().getName());
        }
        if (service instanceof Service) {
            SERVICE_CACHE
                .put(((Service) service).getDescriptorForType().getFullName(), (Service) service);
        }
        if (service instanceof BlockingService) {
            BLOCKING_SERVICE_CACHE
                .put(((BlockingService) service).getDescriptorForType().getFullName(),
                    (BlockingService) service);
        }
        return true;
    }

    @Override public Object get(String serviceName) {
        if (null == serviceName) {
            throw new NullPointerException("serviceName");
        }
        return BLOCKING_SERVICE_CACHE.get(serviceName);
    }
}
