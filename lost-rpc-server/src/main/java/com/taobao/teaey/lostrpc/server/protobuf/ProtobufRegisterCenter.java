package com.taobao.teaey.lostrpc.server.protobuf;

import com.google.protobuf.BlockingService;
import com.google.protobuf.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xiaofei.wxf on 14-2-13.
 */
public class ProtobufRegisterCenter {
    private static final ConcurrentMap<String, BlockingService> BLOCKING_SERVICE_CACHE = new ConcurrentHashMap<String, BlockingService>(1 << 10);
    private static final ConcurrentMap<String, Service> SERVICE_CACHE = new ConcurrentHashMap<String, Service>(1 << 10);

    public static void addService(Object service) {
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

    public static Service removeService(Service service) {
        if (null == service) {
            throw new NullPointerException("service");
        }
        return SERVICE_CACHE.remove(service);
    }

    public static BlockingService removeBlockingService(BlockingService service) {
        if (null == service) {
            throw new NullPointerException("service");
        }
        return BLOCKING_SERVICE_CACHE.remove(service);
    }

    public static BlockingService getBlockingService(String fullname) {
        if (null == fullname) {
            throw new NullPointerException("fullname");
        }
        return BLOCKING_SERVICE_CACHE.get(fullname);
    }

    public static Service getService(String fullname) {
        if (null == fullname) {
            throw new NullPointerException("fullname");
        }
        return SERVICE_CACHE.get(fullname);
    }
}
