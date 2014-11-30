package cn.teaey.lostrpc.server;

import cn.teaey.lostrpc.RpcSession;

/**
 * @author xiaofei.wxf
 */
public interface ServiceInvoker {
    Object handle(RpcSession session, Object packet) throws Exception;
}
