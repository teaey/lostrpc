package com.taobao.teaey.lostrpc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaofei.wxf
 */
public class BaseRpcSession implements RpcSession {
    private static final AtomicInteger id = new AtomicInteger(0);
    private Connection connection;
    private long sessionId;

    private BaseRpcSession(Connection c) {
        this.connection = c;
        this.sessionId = id.incrementAndGet();
    }

    public static RpcSession getOne(Connection c) {
        RpcSession s = SessionCenter.getInstance().get(c.getId());
        if (null == s) {
            s = new BaseRpcSession(c);
            RpcSession tmp = SessionCenter.getInstance().put(s);
            if (null != tmp) {
                s = tmp;
            }
        }
        return s;
    }

    @Override public long connectionId() {
        return connection.getId();
    }

    @Override public long sessionId() {
        return sessionId;
    }

    @Override public boolean close() {
        return false;
    }
}
