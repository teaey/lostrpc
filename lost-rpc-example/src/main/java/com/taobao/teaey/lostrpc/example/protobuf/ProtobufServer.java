package com.taobao.teaey.lostrpc.example.protobuf;

import com.taobao.teaey.lostrpc.common.LostProto;
import com.taobao.teaey.lostrpc.common.ProtobufInitializer;
import com.taobao.teaey.lostrpc.common.Safety;
import com.taobao.teaey.lostrpc.concurrent.AsyncExecutor;
import com.taobao.teaey.lostrpc.server.NettyServer;
import com.taobao.teaey.lostrpc.server.protobuf.ProtobufRegisterCenter;
import com.taobao.teaey.lostrpc.server.protobuf.ServerProtobufDispatcher;
import org.junit.Test;

/**
 * @author xiaofei.wxf
 */
public class ProtobufServer {
    @Test
    public void simpleServer() throws Exception {
        //启动服务器
        ProtobufRegisterCenter.addService(TestProto.LoginService
                .newReflectiveBlockingService(new LoginServiceImpl()));
        NettyServer.newInstance()
                .dispatcher(new ServerProtobufDispatcher(new AsyncExecutor(2)))
                .initializer(ProtobufInitializer.newInstance(Safety.NOT_SAFETY_SERVER,
                        LostProto.Packet.getDefaultInstance()))
                .bind(8888).run();
    }
}
