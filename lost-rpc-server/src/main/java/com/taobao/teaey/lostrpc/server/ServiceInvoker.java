package com.taobao.teaey.lostrpc.server;

import com.taobao.teaey.lostrpc.RpcSession;

/**
 * @author xiaofei.wxf
 */
public interface ServiceInvoker {
    Object handle(RpcSession session, Object packet) throws Exception;
}
