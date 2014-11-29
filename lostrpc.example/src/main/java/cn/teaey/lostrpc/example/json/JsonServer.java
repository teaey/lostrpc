package cn.teaey.lostrpc.example.json;

import cn.teaey.lostrpc.Connection;
import cn.teaey.lostrpc.Ctx;
import cn.teaey.lostrpc.Dispatcher;
import cn.teaey.lostrpc.common.JsonInitializer;
import cn.teaey.lostrpc.common.Safety;
import cn.teaey.lostrpc.server.NettyServer;
import com.alibaba.fastjson.JSON;

/**
 * @author xiaofei.wxf
 */
public class JsonServer {
    public static void main(String[] args) {
        Ctx ctx = new Ctx();
        NettyServer.newInstance().dispatcher(new Dispatcher() {
            @Override
            public void dispatch(Connection o, Object p) {
                System.out.println("收到请求:\n" + JSON.toJSONString(p));
                o.writeAndFlush(p);
            }
        }).initializer(JsonInitializer.newInstance(Safety.NOT_SAFETY_SERVER, ctx)).bind(8888).run();
    }
}
