package cn.teaey.lostrpc.example.protobuf;

import cn.teaey.lostrpc.Ctx;
import cn.teaey.lostrpc.common.LostProto;
import cn.teaey.lostrpc.common.ProtobufInitializer;
import cn.teaey.lostrpc.common.Safety;
import cn.teaey.lostrpc.concurrent.AsyncExecutor;
import cn.teaey.lostrpc.server.NettyServer;
import cn.teaey.lostrpc.server.ServiceInvokerDispatcher;
import cn.teaey.lostrpc.server.protobuf.ProtobufServiceCenter;
import cn.teaey.lostrpc.server.protobuf.ProtobufServiceInvoker;
import org.junit.Test;

/**
 * @author xiaofei.wxf
 */
public class ProtobufServer {
    @Test
    public void simpleServer() throws Exception {
        Ctx ctx = new Ctx();
        //启动服务器
        ProtobufServiceCenter.theOne().add(TestProto.LoginService
            .newReflectiveBlockingService(new LoginServiceImpl()));
        NettyServer.newInstance()
            .dispatcher(ServiceInvokerDispatcher
                .newOne(AsyncExecutor.newOne(2), ProtobufServiceInvoker.theOne()))
            .initializer(ProtobufInitializer.newInstance(Safety.NOT_SAFETY_SERVER, ctx,
                LostProto.Packet.getDefaultInstance()))
            .bind(8881).run();
    }
}
