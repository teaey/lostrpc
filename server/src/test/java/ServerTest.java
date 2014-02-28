import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.common.JsonInitializer;
import com.taobao.teaey.lostrpc.server.NettyServer;
import io.netty.channel.Channel;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by xiaofei.wxf on 14-2-14.
 */
public class ServerTest {

    @Test
    public void jsonServer() throws IOException, InterruptedException {
        NettyServer.newInstance().initializer(JsonInitializer.newInstance()).dispatcher(new Dispatcher<Channel, Object>() {
            @Override
            public void dispatch(Channel o, Object p) {
                o.writeAndFlush(p);
            }
        }).bind(8888).run();
    }
}
