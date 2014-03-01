package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.Dispatcher;
import io.netty.channel.Channel;

/**
 * @author xiaofei.wxf
 */
public abstract class SyncDispatcher<MsgType> implements Dispatcher<Channel, MsgType> {
    @Override
    public abstract void dispatch(Channel channel, MsgType p);
}
