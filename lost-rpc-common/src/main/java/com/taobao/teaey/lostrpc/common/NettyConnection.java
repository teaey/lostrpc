package com.taobao.teaey.lostrpc.common;

import io.netty.channel.Channel;

/**
 * @author xiaofei.wxf
 */
public class NettyConnection implements Connection {
    private final Channel channel;

    public NettyConnection(Channel channel) {
        this.channel = channel;
    }

    @Override
    public int getId() {
        return this.channel.hashCode();
    }

    @Override
    public void writeAndFlush(Object msg) {
        this.channel.writeAndFlush(msg);
    }
}
