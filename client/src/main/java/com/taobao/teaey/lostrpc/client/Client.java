package com.taobao.teaey.lostrpc.client;

import com.taobao.teaey.lostrpc.Dispatcher;

import java.net.InetSocketAddress;

/**
 * Created by xiaofei.wxf on 14-2-13.
 */
public interface Client<ReqType, RespType, Channel, T extends Client> {

    T run();

    T shutdown();

    T connect(InetSocketAddress address);

    T showdownNow();

    Object ask(ReqType p);

    T dispatcher(Dispatcher dispatcher);

}
