package com.taobao.teaey.lostrpc.example;

import com.alibaba.fastjson.JSON;
import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.common.JsonInitializer;
import com.taobao.teaey.lostrpc.server.NettyServer;

/**
 * @author xiaofei.wxf
 */
public class JsonServerTest {
    public static void main(String[] args) {
        NettyServer.newInstance().dispatcher(new Dispatcher() {
            @Override
            public void dispatch(Object o, Object p) {
                System.out.println(JSON.toJSONString(p));
            }
        }).initializer(JsonInitializer.newInstance()).bind(8888).run();
    }
}
