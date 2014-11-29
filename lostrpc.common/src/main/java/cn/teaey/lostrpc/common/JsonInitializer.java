package cn.teaey.lostrpc.common;

import cn.teaey.lostrpc.Ctx;
import cn.teaey.lostrpc.NettyChannelInitializer;
import cn.teaey.lostrpc.codec.JsonCodec;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaofei.wxf
 */
public class JsonInitializer extends NettyChannelInitializer {

    private final static Logger logger = LoggerFactory.getLogger(NettyChannelInitializer.class);
    private final ChannelHandler encoder;

    private JsonInitializer(Safety safety, Ctx ctx, ChannelHandler... handlers) {
        super(safety, ctx, handlers);
        this.encoder = FrameCodec.newEncoder(getSafety(), JsonCodec.instance(), ctx);
    }

    public static NettyChannelInitializer newInstance(Safety safety, Ctx ctx,
        ChannelHandler... handler) {
        return new JsonInitializer(safety, ctx, handler);
    }

    public static NettyChannelInitializer newInstance(Ctx ctx, ChannelHandler... handler) {
        return new JsonInitializer(Safety.NOT_SAFETY_CLIENT, ctx, handler);
    }

    @Override
    protected void decoders(Channel ch, Ctx ctx) throws Exception {
        ch.pipeline()
            .addLast("decoder", FrameCodec.newDecoder(getSafety(), JsonCodec.instance(), ctx));
    }

    @Override
    protected void encoders(Channel ch, Ctx ctx) throws Exception {
        ch.pipeline().addLast("encoder", encoder);
    }

    @Override
    protected void handlers(Channel ch, Ctx ctx) throws Exception {
        for (int i = 1; i <= handlers.length; i++) {
            ch.pipeline().addLast("handler-" + i, handlers[i - 1]);
        }
    }

    @Override
    protected Logger getLogger(Ctx ctx) {
        return logger;
    }
}
