package cn.teaey.lostrpc;

import cn.teaey.lostrpc.common.DispatchHandler;
import cn.teaey.lostrpc.common.Safety;
import cn.teaey.lostrpc.common.TightLoggingHandler;
import cn.teaey.lostrpc.common.DispatchHandler;
import cn.teaey.lostrpc.common.Safety;
import cn.teaey.lostrpc.common.TightLoggingHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import org.slf4j.Logger;

/**
 * @author xiaofei.wxf on 14-2-14.
 */
public abstract class NettyChannelInitializer extends ChannelInitializer {

    /**
     * 如果SLF4J的日志级别为debug，会自动在业务处理handler前加上debug日志handler
     */
    private static final ChannelHandler LOGGING_HANDLER = new TightLoggingHandler();

    protected final ChannelHandler[] handlers;

    private final Safety safety;
    private final Ctx ctx;
    private DispatchHandler bridgeHandler;

    public NettyChannelInitializer(Safety safety, Ctx ctx, ChannelHandler... handlers) {
        this.safety = safety;
        this.handlers = handlers;
        this.ctx = ctx;
    }

    public Safety getSafety() {
        return safety;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        decoders(ch, ctx);
        encoders(ch, ctx);
        if (getLogger(ctx).isDebugEnabled()) {
            ch.pipeline().addLast("LOGGING_HANDLER", LOGGING_HANDLER);
        }
        handlers(ch, ctx);
        ch.pipeline().addLast(this.bridgeHandler);
    }

    protected abstract void decoders(Channel ch, Ctx ctx) throws Exception;

    protected abstract void encoders(Channel ch, Ctx ctx) throws Exception;

    protected abstract void handlers(Channel ch, Ctx ctx) throws Exception;

    protected abstract Logger getLogger(Ctx ctx);

    public void bridgeHandler(DispatchHandler dispatcher) {
        this.bridgeHandler = dispatcher;
    }
}
