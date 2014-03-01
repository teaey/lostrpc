package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.Dispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaofei.wxf on 14-2-13.
 */
@ChannelHandler.Sharable
public class DispatchHandler<Type> extends ChannelInboundHandlerAdapter implements Dispatcher<Channel, Type> {
    private static final Logger logger = LoggerFactory.getLogger(DispatchHandler.class);
    protected final Dispatcher<Channel, Type> dispatcher;

    public DispatchHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            dispatch(ctx.channel(), (Type) msg);
        } catch (ClassCastException e) {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("EXCEPTION CAUGHT:\n", cause);
    }

    @Override
    public void dispatch(Channel channel, Type p) {
        this.dispatcher.dispatch(channel, p);
    }
}
