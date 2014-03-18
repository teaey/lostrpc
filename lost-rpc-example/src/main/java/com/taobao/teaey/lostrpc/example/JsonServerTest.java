package com.taobao.teaey.lostrpc.example;

import com.alibaba.fastjson.JSON;
import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.common.Connection;
import com.taobao.teaey.lostrpc.common.JsonInitializer;
import com.taobao.teaey.lostrpc.common.Safety;
import com.taobao.teaey.lostrpc.server.NettyServer;

/**
 * @author xiaofei.wxf
 */
public class JsonServerTest {
    public static void main(String[] args) {
        NettyServer.newInstance().dispatcher(new Dispatcher() {
            @Override
            public void dispatch(Connection o, Object p) {
                System.out.println(JSON.toJSONString(p));
            }
        }).initializer(JsonInitializer.newInstance(Safety.NOT_SAFETY_SERVER)).bind(8888).run();
    }
}
