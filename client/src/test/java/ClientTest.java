import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.client.Client;
import com.taobao.teaey.lostrpc.client.NettyClient;
import com.taobao.teaey.lostrpc.common.JsonInitializer;
import io.netty.channel.Channel;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * Created by xiaofei.wxf on 14-2-14.
 */
public class ClientTest {

    @Test
    public void jsonClient() throws InterruptedException {
        Client client =
                NettyClient.newInstance().initializer(JsonInitializer.newInstance()).dispatcher(new Dispatcher<Channel, Object>() {
                    @Override
                    public void dispatch(Channel channel, Object p) {
                        System.out.println("来自服务器的响应:" + p);
                    }
                }).connect(new InetSocketAddress(8888)).run();
        while (true) {
            client.ask(new Integer(1));
            Thread.sleep(3000);
        }

    }
}
