package cn.teaey.lostrpc.safety;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xiaofei.wxf
 */
public class DefaultServerSafeHandler extends ChannelInboundHandlerAdapter {
    private static final AttributeKey<AtomicBoolean> AUTHED =
        AttributeKey.valueOf("SafeHandler-AUTHED");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Attribute<AtomicBoolean> attr = ctx.attr(AUTHED);
        if (attr.get() == null) {
            attr.set(new AtomicBoolean(false));
        }
        AtomicBoolean authed = attr.get();
        if (authed.get()) {
            return;
        }


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}
