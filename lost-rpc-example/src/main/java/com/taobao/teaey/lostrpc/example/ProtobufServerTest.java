package com.taobao.teaey.lostrpc.example;

import com.taobao.teaey.lostrpc.TestProto;
import com.taobao.teaey.lostrpc.common.CustomExecutor;
import com.taobao.teaey.lostrpc.common.LostProto;
import com.taobao.teaey.lostrpc.common.ProtobufInitializer;
import com.taobao.teaey.lostrpc.server.NettyServer;
import com.taobao.teaey.lostrpc.server.protobuf.ProtobufRegisterCenter;
import com.taobao.teaey.lostrpc.server.protobuf.ServerProtobufDispatcher;

/**
 * @author xiaofei.wxf
 */
public class ProtobufServerTest {
    public static void main(String[] args) {
        ProtobufRegisterCenter.addService(TestProto.LoginService.newReflectiveBlockingService(new LoginServiceImpl()));
        NettyServer.newInstance().dispatcher(new ServerProtobufDispatcher(new CustomExecutor.ModulusExecutor(2))).initializer(ProtobufInitializer.newInstance(LostProto.Packet.getDefaultInstance())).bind(8888).run();
    }
}
