package cn.teaey.lostrpc.server;


import cn.teaey.lostrpc.BaseRpcSession;
import cn.teaey.lostrpc.Connection;
import cn.teaey.lostrpc.RpcSession;
import cn.teaey.lostrpc.common.LostProto;
import cn.teaey.lostrpc.dispatcher.CustomDispatcher;

import java.util.concurrent.Executor;

/**
 * @author xiaofei.wxf
 */
public class ServiceInvokerDispatcher extends CustomDispatcher<LostProto.Packet> {
    private final ServiceInvoker serviceInvoker;

    private ServiceInvokerDispatcher(Executor executor, ServiceInvoker center) {
        super(executor);
        if (null == center) {
            throw new NullPointerException("center");
        }
        this.serviceInvoker = center;
    }

    public static ServiceInvokerDispatcher newOne(Executor executor, ServiceInvoker center) {
        return new ServiceInvokerDispatcher(executor, center);
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
