package com.taobao.teaey.lostrpc.example.protobuf;

import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.client.NettyClient;
import com.taobao.teaey.lostrpc.common.Connection;
import com.taobao.teaey.lostrpc.common.LostProto;
import com.taobao.teaey.lostrpc.common.ProtobufInitializer;
import com.taobao.teaey.lostrpc.common.Safety;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiaofei.wxf
 */
public class ProtobufClient {
    private volatile CountDownLatch cc = new CountDownLatch(1);

    @Test
    public void simpleClient() throws IOException {
        NettyClient client = NettyClient.newInstance()
                .initializer(ProtobufInitializer.
                        newInstance(Safety.NOT_SAFETY_CLIENT, LostProto.Packet.getDefaultInstance()))
                .dispatcher(new Dispatcher() {
                    @Override
                    public void dispatch(Connection o, Object p) {
                        System.out.println("响应数据:\n" + p);
                        cc.countDown();
                    }
                }).connect(new InetSocketAddress(8888)).run();


        for (int i = 0; i < 1000; i++) {
            cc = new CountDownLatch(1);
            client.ask(LostProto.Packet.newBuilder().setPId(i).setMethodName("login").setServiceName("com.taobao.teaey.lostrpc.LoginService").setTimestamp(System.currentTimeMillis()).setData(TestProto.Login_C2S.newBuilder().setTimestamp(System.currentTimeMillis()).build().toByteString()));
            try {
                cc.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
