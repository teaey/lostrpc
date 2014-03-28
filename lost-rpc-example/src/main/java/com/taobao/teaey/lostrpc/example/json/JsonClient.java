package com.taobao.teaey.lostrpc.example.json;

import com.alibaba.fastjson.JSON;
import com.taobao.teaey.lostrpc.Connection;
import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.client.NettyClient;
import com.taobao.teaey.lostrpc.common.JsonInitializer;
import com.taobao.teaey.lostrpc.common.Safety;
import io.netty.channel.ChannelFuture;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @author xiaofei.wxf
 */
public class JsonClient {
    @Test
    public void simpleClient() {
        NettyClient client = NettyClient.newInstance().dispatcher(new Dispatcher() {
            @Override
            public void dispatch(Connection o, Object p) {
                System.out.println("收到响应:\n" + JSON.toJSONString(p));
            }
        }).initializer(JsonInitializer.newInstance(Safety.NOT_SAFETY_CLIENT))
            .connect(new InetSocketAddress(8888)).run();
        try {
            ChannelFuture future = client.ask(new Date());
            future.sync();
            if (!future.isSuccess()) {
                System.out.println(future.cause());
            }
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
