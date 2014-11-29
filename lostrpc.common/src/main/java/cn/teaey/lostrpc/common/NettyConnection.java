package cn.teaey.lostrpc.common;

import cn.teaey.lostrpc.Connection;
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
    public long getId() {
        return this.channel.hashCode();
    }

    @Override
    public void writeAndFlush(Object msg) {
        this.channel.writeAndFlush(msg);
    }
}
