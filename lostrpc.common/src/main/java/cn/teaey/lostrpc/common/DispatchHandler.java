package cn.teaey.lostrpc.common;

import cn.teaey.lostrpc.Dispatcher;
import cn.teaey.lostrpc.Connection;
import cn.teaey.lostrpc.Dispatcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * 先看下整个消息处理层级:
 *
 * @author xiaofei.wxf on 14-2-13.
 * @see Readme
 * DispatchHandler是网络层与消息分发层的桥梁
 */
@ChannelHandler.Sharable
public class DispatchHandler<Type> extends ChannelInboundHandlerAdapter
    implements Dispatcher<Type> {
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
        Throwable e = cause;
        while (true) {
            if (e instanceof Safety.UnauthException || e instanceof Safety.DecryptException) {
                ctx.close();
                break;
            }
            e = e.getCause();
            if (null == e) {
                break;
            }
        }
    }

    @Override public void dispatch(Connection c, Type p) {
        this.dispatcher.dispatch(c, p);
    }
}
