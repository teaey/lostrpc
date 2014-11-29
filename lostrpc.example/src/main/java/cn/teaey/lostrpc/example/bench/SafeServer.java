package cn.teaey.lostrpc.example.bench;

import cn.teaey.lostrpc.Ctx;
import cn.teaey.lostrpc.common.LostProto;
import cn.teaey.lostrpc.common.ProtobufInitializer;
import cn.teaey.lostrpc.common.Safety;
import cn.teaey.lostrpc.concurrent.AsyncExecutor;
import cn.teaey.lostrpc.example.protobuf.LoginServiceImpl;
import cn.teaey.lostrpc.example.protobuf.ProtobufServer;
import cn.teaey.lostrpc.example.protobuf.TestProto;
import cn.teaey.lostrpc.safety.RSAEnAndDecryption;
import cn.teaey.lostrpc.server.NettyServer;
import cn.teaey.lostrpc.server.ServiceInvokerDispatcher;
import cn.teaey.lostrpc.server.protobuf.ProtobufServiceCenter;
import cn.teaey.lostrpc.server.protobuf.ProtobufServiceInvoker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author xiaofei.wxf on 2014/3/19.
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
        Ctx ctx = new Ctx();
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
                ProtobufServiceInvoker.theOne()))
            .initializer(ProtobufInitializer.newInstance(safety, ctx,
                LostProto.Packet.getDefaultInstance()))
            .bind(8888).run();
    }

    @Test
    public void nosafetyServer() throws Exception {
        Ctx ctx = new Ctx();
        //启动服务器
        ProtobufServiceCenter.theOne().add(TestProto.LoginService
            .newReflectiveBlockingService(new LoginServiceImpl()));
        NettyServer.newInstance()
            .dispatcher(ServiceInvokerDispatcher
                .newOne(AsyncExecutor.newOne(2), ProtobufServiceInvoker.theOne()))
            .initializer(ProtobufInitializer.newInstance(Safety.NOT_SAFETY_SERVER,ctx,
                LostProto.Packet.getDefaultInstance()))
            .bind(8888).run();
    }
}
