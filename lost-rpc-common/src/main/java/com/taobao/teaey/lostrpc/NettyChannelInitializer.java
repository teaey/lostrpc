package com.taobao.teaey.lostrpc;

import com.taobao.teaey.lostrpc.common.DispatchHandler;
import com.taobao.teaey.lostrpc.common.Safety;
import com.taobao.teaey.lostrpc.common.TightLoggingHandler;
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

    public NettyChannelInitializer(Safety safety, ChannelHandler... handlers) {
        this.safety = safety;
        this.handlers = handlers;
    }

    public Safety getSafety() {
        return safety;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        decoders(ch);
        encoders(ch);
        if (getLogger().isDebugEnabled()) {
            ch.pipeline().addLast("LOGGING_HANDLER", LOGGING_HANDLER);
        }
        handlers(ch);
        ch.pipeline().addLast(this.bridgeHandler);
    }

    private DispatchHandler bridgeHandler;

    protected abstract void decoders(Channel ch) throws Exception;

    protected abstract void encoders(Channel ch) throws Exception;

    protected abstract void handlers(Channel ch) throws Exception;

    protected abstract Logger getLogger();

    public void bridgeHandler(DispatchHandler dispatcher) {
        this.bridgeHandler = dispatcher;
    }
}
