package com.taobao.teaey.lostrpc.samples;

import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.TestProto;
import com.taobao.teaey.lostrpc.client.Client;
import com.taobao.teaey.lostrpc.client.NettyClient;
import com.taobao.teaey.lostrpc.common.LostProto;
import com.taobao.teaey.lostrpc.common.ProtobufInitializer;

import java.net.InetSocketAddress;

/**
 * @author xiaofei.wxf
 */
public class ProtobufClientTest {
    public static void main(String[] args) {
        Client client = NettyClient.newInstance().initializer(ProtobufInitializer.newInstance(LostProto.Packet.getDefaultInstance())).dispatcher(new Dispatcher() {
            @Override
            public void dispatch(Object o, Object p) {
                System.out.println(p);
            }
        }).connect(new InetSocketAddress(8888)).run();
        long s1 = System.currentTimeMillis();
        for(int i=0;i<100000;i++){
            client.ask(LostProto.Packet.newBuilder().setPId(1).setMethodName("login").setServiceName("com.taobao.teaey.lostrpc.LoginService").setTimestamp(System.currentTimeMillis()).setData(TestProto.Login_C2S.newBuilder().setTimestamp(System.currentTimeMillis()).build().toByteString()));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
