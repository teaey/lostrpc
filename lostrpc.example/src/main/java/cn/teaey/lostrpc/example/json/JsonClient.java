package cn.teaey.lostrpc.example.json;

import cn.teaey.lostrpc.Connection;
import cn.teaey.lostrpc.Ctx;
import cn.teaey.lostrpc.Dispatcher;
import cn.teaey.lostrpc.client.NettyClient;
import cn.teaey.lostrpc.common.JsonInitializer;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;

/**
 * @author xiaofei.wxf
 */
public class JsonClient {
    public static void main(String[] args) throws InterruptedException {
        Ctx ctx = new Ctx();
        NettyClient client = NettyClient.newInstance().dispatcher(new Dispatcher() {
            @Override
            public void dispatch(Connection o, Object p) {
                System.out.println("收到响应:\n" + JSON.toJSONString(p));
            }
        }).initializer(JsonInitializer.newInstance(ctx))
            .connect(new InetSocketAddress(8888)).run();
        ChannelFuture future = client.ask("abc");
        future.sync();
        if (!future.isSuccess()) {
            System.out.println(future.cause());
        }
    }
}
