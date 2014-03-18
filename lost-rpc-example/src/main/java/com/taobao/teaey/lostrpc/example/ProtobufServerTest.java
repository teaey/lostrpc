package com.taobao.teaey.lostrpc.example;

import com.taobao.teaey.lostrpc.TestProto;
import com.taobao.teaey.lostrpc.common.LostProto;
import com.taobao.teaey.lostrpc.common.ProtobufInitializer;
import com.taobao.teaey.lostrpc.common.Safety;
import com.taobao.teaey.lostrpc.concurrent.AsyncExecutor;
import com.taobao.teaey.lostrpc.safety.RSAEnAndDecryption;
import com.taobao.teaey.lostrpc.server.NettyServer;
import com.taobao.teaey.lostrpc.server.protobuf.ProtobufRegisterCenter;
import com.taobao.teaey.lostrpc.server.protobuf.ServerProtobufDispatcher;

/**
 * @author xiaofei.wxf
 */
public class ProtobufServerTest {
    public static void main(String[] args) throws Exception {
        RSAEnAndDecryption rsa = new RSAEnAndDecryption();
        rsa.loadPrivkeyFromFile("D:\\Workspace\\test-all-in-one\\encryption\\src\\main\\resources\\rsa_pkcs8.pem");
        rsa.loadPubkeyFromFile("D:\\Workspace\\test-all-in-one\\encryption\\src\\main\\resources\\rsapubkey.pem");
        Safety safety = Safety.newServerSafety();
        safety.setRsa(rsa);
        ProtobufRegisterCenter.addService(TestProto.LoginService
                .newReflectiveBlockingService(new LoginServiceImpl()));
        NettyServer.newInstance()
                .dispatcher(new ServerProtobufDispatcher(new AsyncExecutor(2)))
                .initializer(ProtobufInitializer.newInstance(safety,
                        LostProto.Packet.getDefaultInstance()))
                .bind(8888).run();
    }
}
