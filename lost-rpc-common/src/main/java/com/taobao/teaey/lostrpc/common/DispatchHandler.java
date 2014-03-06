package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.Dispatcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaofei.wxf on 14-2-13.
 */
@ChannelHandler.Sharable
public class DispatchHandler<Type> extends ChannelInboundHandlerAdapter implements Dispatcher<Type> {
    private static final Logger logger = LoggerFactory.getLogger(DispatchHandler.class);
    private static final AttributeKey<Connection> ConnKey = AttributeKey.valueOf("ConnKey");
    protected final Dispatcher<Type> dispatcher;

    public DispatchHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            dispatch(getConn(ctx), (Type) msg);
        } catch (ClassCastException e) {
            ctx.fireChannelRead(msg);
        }
    }

    Connection getConn(ChannelHandlerContext ctx) {
        Attribute<Connection> attr = ctx.attr(ConnKey);
        Connection conn = attr.get();
        if (conn == null) {
            conn = new NettyConnection(ctx.channel());
            attr.set(conn);
        }
        return conn;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("EXCEPTION CAUGHT:\n", cause);
    }

    @Override
    public void dispatch(Connection channel, Type p) {
        this.dispatcher.dispatch(channel, p);
    }
}
