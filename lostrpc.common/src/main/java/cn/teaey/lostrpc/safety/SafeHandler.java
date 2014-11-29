package cn.teaey.lostrpc.safety;

import cn.teaey.lostrpc.common.Safety;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xiaofei.wxf
 */
public class SafeHandler extends ChannelInboundHandlerAdapter {
    private final Safety safety;

    public SafeHandler(Safety safety) {
        this.safety = safety;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (safety.isSafety() && !safety.isServer()) {
            doHandshake(ctx);
        }
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    public void doHandshake(ChannelHandlerContext ctx) {

    }
}
