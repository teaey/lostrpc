package com.taobao.teaey.lostrpc.example;

import com.taobao.teaey.lostrpc.Cmd;
import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.TestProto;
import com.taobao.teaey.lostrpc.client.Client;
import com.taobao.teaey.lostrpc.client.NettyClient;
import com.taobao.teaey.lostrpc.common.Connection;
import com.taobao.teaey.lostrpc.common.LostProto;
import com.taobao.teaey.lostrpc.common.ProtobufInitializer;
import com.taobao.teaey.lostrpc.common.Safety;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author xiaofei.wxf
 */
public class ProtobufClientTest {
    public static void main(String[] args) throws IOException {
        Client client = NettyClient.newInstance()
                .initializer(ProtobufInitializer.newInstance(Safety.newClientSafety(), LostProto.Packet.getDefaultInstance()))
                .dispatcher(new Dispatcher() {
                    @Override
                    public void dispatch(Connection o, Object p) {
                        System.out.println(p);
                    }
                }).connect(new InetSocketAddress(8888)).run();

        ByteBuf buf = Unpooled.buffer(5);
        buf.writeInt(0);
        buf.writeByte(Cmd.CMD_SYNCKEY_REQ.getType());
        client.ask(buf);
        System.in.read();
        for (int i = 0; i < 100000; i++) {
            client.ask(LostProto.Packet.newBuilder().setPId(1).setMethodName("login").setServiceName("com.taobao.teaey.lostrpc.LoginService").setTimestamp(System.currentTimeMillis()).setData(TestProto.Login_C2S.newBuilder().setTimestamp(System.currentTimeMillis()).build().toByteString()));
            try {
                Thread.sleep(200000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
