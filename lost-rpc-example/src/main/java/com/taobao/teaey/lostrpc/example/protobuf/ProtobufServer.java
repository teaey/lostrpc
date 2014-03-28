package com.taobao.teaey.lostrpc.example.protobuf;

import com.taobao.teaey.lostrpc.common.LostProto;
import com.taobao.teaey.lostrpc.common.ProtobufInitializer;
import com.taobao.teaey.lostrpc.common.Safety;
import com.taobao.teaey.lostrpc.concurrent.AsyncExecutor;
import com.taobao.teaey.lostrpc.server.NettyServer;
import com.taobao.teaey.lostrpc.server.ServiceInvokerDispatcher;
import com.taobao.teaey.lostrpc.server.protobuf.ProtobufServiceCenter;
import com.taobao.teaey.lostrpc.server.protobuf.ProtobufServiceInvoker;
import org.junit.Test;

/**
 * @author xiaofei.wxf
 */
public class ProtobufServer {
    @Test
    public void simpleServer() throws Exception {
        //启动服务器
        ProtobufServiceCenter.getInstance().add(TestProto.LoginService
            .newReflectiveBlockingService(new LoginServiceImpl()));
        NettyServer.newInstance()
            .dispatcher(ServiceInvokerDispatcher
                .newOne(AsyncExecutor.newOne(2), new ProtobufServiceInvoker()))
            .initializer(ProtobufInitializer.newInstance(Safety.NOT_SAFETY_SERVER,
                LostProto.Packet.getDefaultInstance()))
            .bind(8881).run();
    }
}
