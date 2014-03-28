package com.taobao.teaey.lostrpc.server;

import com.taobao.teaey.lostrpc.BaseRpcSession;
import com.taobao.teaey.lostrpc.Connection;
import com.taobao.teaey.lostrpc.RpcSession;
import com.taobao.teaey.lostrpc.common.LostProto;
import com.taobao.teaey.lostrpc.dispatcher.CustomDispatcher;

import java.util.concurrent.Executor;

/**
 * @author xiaofei.wxf
 */
public class ServiceInvokerDispatcher extends CustomDispatcher<LostProto.Packet> {
    public static ServiceInvokerDispatcher newOne(Executor executor, ServiceInvoker center) {
        return new ServiceInvokerDispatcher(executor, center);
    }

    private final ServiceInvoker serviceInvoker;

    private ServiceInvokerDispatcher(Executor executor, ServiceInvoker center) {
        super(executor);
        if (null == center) {
            throw new NullPointerException("center");
        }
        this.serviceInvoker = center;
    }

    public void customDispatch(Connection c, LostProto.Packet p) throws Exception {
        RpcSession session = BaseRpcSession.getOne(c);
        Object resp = this.serviceInvoker.handle(session, p);
        if (null == resp) {
            return;
        }
        c.writeAndFlush(resp);
    }
}
