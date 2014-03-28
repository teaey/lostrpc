package com.taobao.teaey.lostrpc.example.json;

import com.alibaba.fastjson.JSON;
import com.taobao.teaey.lostrpc.Connection;
import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.common.JsonInitializer;
import com.taobao.teaey.lostrpc.common.Safety;
import com.taobao.teaey.lostrpc.server.NettyServer;
import org.junit.Test;

/**
 * @author xiaofei.wxf
 */
public class JsonServer {

    @Test
    public void simpleServer() {
        NettyServer.newInstance().dispatcher(new Dispatcher() {
            @Override
            public void dispatch(Connection o, Object p) {
                System.out.println("收到请求:\n" + JSON.toJSONString(p));
                o.writeAndFlush(p);
            }
        }).initializer(JsonInitializer.newInstance(Safety.NOT_SAFETY_SERVER)).bind(8888).run();
    }
}
