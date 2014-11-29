package cn.teaey.lostrpc.example.protobuf;

import cn.teaey.lostrpc.Connection;
import cn.teaey.lostrpc.Ctx;
import cn.teaey.lostrpc.Dispatcher;
import cn.teaey.lostrpc.client.NettyClient;
import cn.teaey.lostrpc.common.LostProto;
import cn.teaey.lostrpc.common.ProtobufInitializer;
import cn.teaey.lostrpc.common.Safety;
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
        Ctx ctx = new Ctx();
        NettyClient client = NettyClient.newInstance()
            .initializer(ProtobufInitializer.
                newInstance(Safety.NOT_SAFETY_CLIENT, ctx, LostProto.Packet.getDefaultInstance()))
            .dispatcher(new Dispatcher() {
                @Override
                public void dispatch(Connection o, Object p) {
                    System.out.println("响应数据:\n" + p);
                    cc.countDown();
                }
            }).connect(new InetSocketAddress(8881)).run();


        for (int i = 0; i < 1; i++) {
            cc = new CountDownLatch(1);
            client.ask(LostProto.Packet.newBuilder().setPId(i).setMethodName("login")
                .setServiceName("LoginService")
                .setTimestamp(System.currentTimeMillis()).setData(
                    TestProto.Login_C2S.newBuilder().setTimestamp(System.currentTimeMillis())
                        .build().toByteString()));
            try {
                cc.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
