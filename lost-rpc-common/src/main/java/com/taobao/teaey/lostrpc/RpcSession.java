package com.taobao.teaey.lostrpc;

/**
 * @author xiaofei.wxf
 */
public interface RpcSession {
    long connectionId();

    long sessionId();

    boolean close();
}
