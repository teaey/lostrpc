package com.taobao.teaey.lostrpc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xiaofei.wxf
 */
public class SessionCenter {
    private SessionCenter() {
    }

    static class Holder {
        private static final SessionCenter i = new SessionCenter();
    }

    public static SessionCenter theOne() {
        return Holder.i;
    }

    private final ConcurrentMap<Long, RpcSession> map = new ConcurrentHashMap<Long, RpcSession>();

    public RpcSession put(RpcSession rpcSession) {
        return map.putIfAbsent(rpcSession.sessionId(), rpcSession);
    }

    public RpcSession get(long sessionId) {
        return map.get(sessionId);
    }
}
