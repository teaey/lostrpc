package com.taobao.teaey.lostrpc.example.bench;

import com.taobao.teaey.lostrpc.common.LostProto;
import com.taobao.teaey.lostrpc.common.ProtobufInitializer;
import com.taobao.teaey.lostrpc.common.Safety;
import com.taobao.teaey.lostrpc.concurrent.AsyncExecutor;
import com.taobao.teaey.lostrpc.example.protobuf.LoginServiceImpl;
import com.taobao.teaey.lostrpc.example.protobuf.ProtobufServer;
import com.taobao.teaey.lostrpc.example.protobuf.TestProto;
import com.taobao.teaey.lostrpc.safety.RSAEnAndDecryption;
import com.taobao.teaey.lostrpc.server.NettyServer;
import com.taobao.teaey.lostrpc.server.ServiceInvokerDispatcher;
import com.taobao.teaey.lostrpc.server.protobuf.ProtobufServiceCenter;
import com.taobao.teaey.lostrpc.server.protobuf.ProtobufServiceInvoker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by xiaofei.wxf on 2014/3/19.
 */
public class SafeServer {

    @Before
    public void setup() {

    }

    @After
    public void cleanup() {

    }

    @Test
    public void safetyServer() throws Exception {
        //准备Key
        RSAEnAndDecryption rsa = new RSAEnAndDecryption();
        rsa.loadPrivkeyFromInputStream(
            ProtobufServer.class.getClassLoader().getResourceAsStream("rsa_priv_pkcs8.pem"));
        rsa.loadPubkeyFromInputStream(
            ProtobufServer.class.getClassLoader().getResourceAsStream("rsa_pub.pem"));
        rsa.initPrivKeySign();
        Safety safety = Safety.newServerSafety();
        safety.setRsa(rsa);
        //启动服务器
        ProtobufServiceCenter.theOne().add(TestProto.LoginService
            .newReflectiveBlockingService(new LoginServiceImpl()));
        NettyServer.newInstance()
            .dispatcher(ServiceInvokerDispatcher.newOne(AsyncExecutor.newOne(2),
                new ProtobufServiceInvoker()))
            .initializer(ProtobufInitializer.newInstance(safety,
                LostProto.Packet.getDefaultInstance()))
            .bind(8888).run();
    }

    @Test
    public void nosafetyServer() throws Exception {
        //启动服务器
        ProtobufServiceCenter.theOne().add(TestProto.LoginService
            .newReflectiveBlockingService(new LoginServiceImpl()));
        NettyServer.newInstance()
            .dispatcher(ServiceInvokerDispatcher
                .newOne(AsyncExecutor.newOne(2), new ProtobufServiceInvoker()))
            .initializer(ProtobufInitializer.newInstance(Safety.NOT_SAFETY_SERVER,
                LostProto.Packet.getDefaultInstance()))
            .bind(8888).run();
    }
}
