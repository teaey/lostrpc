package cn.teaey.lostrpc.server;

import cn.teaey.lostrpc.Dispatcher;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaofei.wxf on 14-2-13.
 */
public interface Server<Channel, ReqType, T extends Server> {
    AtomicInteger IDX = new AtomicInteger();

    T run();

    T shutdown();

    T bind(int port);

    T bind(SocketAddress address);

    T showdownNow();

    T dispatcher(Dispatcher<ReqType> dispatcher);

}
