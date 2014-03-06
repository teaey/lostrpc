package com.taobao.teaey.lostrpc.server;

import com.taobao.teaey.lostrpc.Dispatcher;

import java.net.SocketAddress;

/**
 * Created by xiaofei.wxf on 14-2-13.
 */
public interface Server<Channel, ReqType, T extends Server> {

    T run();

    T shutdown();

    T bind(int port);

    T bind(SocketAddress address);

    T showdownNow();

    T dispatcher(Dispatcher<ReqType> dispatcher);

}
