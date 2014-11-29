package cn.teaey.lostrpc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xiaofei.wxf
 */
public class SessionCenter {
    private final ConcurrentMap<Long, RpcSession> map = new ConcurrentHashMap<Long, RpcSession>();

    private SessionCenter() {
    }

    public static SessionCenter theOne() {
        return Holder.i;
    }

    public RpcSession put(RpcSession rpcSession) {
        return map.putIfAbsent(rpcSession.sessionId(), rpcSession);
    }

    public RpcSession get(long sessionId) {
        return map.get(sessionId);
    }


    static class Holder {
        private static final SessionCenter i = new SessionCenter();
    }
}
