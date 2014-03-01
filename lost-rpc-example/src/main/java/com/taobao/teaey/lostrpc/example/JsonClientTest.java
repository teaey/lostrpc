package com.taobao.teaey.lostrpc.example;

import com.alibaba.fastjson.JSON;
import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.client.NettyClient;
import com.taobao.teaey.lostrpc.common.JsonInitializer;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @author xiaofei.wxf
 */
public class JsonClientTest {
    public static void main(String[] args) {
        NettyClient client = NettyClient.newInstance().dispatcher(new Dispatcher() {
            @Override
            public void dispatch(Object o, Object p) {
                System.out.println(JSON.toJSONString(p));
            }
        }).initializer(JsonInitializer.newInstance()).connect(new InetSocketAddress(8888)).run();
        while (true) {
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
}
