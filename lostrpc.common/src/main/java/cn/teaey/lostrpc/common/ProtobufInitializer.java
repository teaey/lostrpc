package cn.teaey.lostrpc.common;

import cn.teaey.lostrpc.Ctx;
import cn.teaey.lostrpc.NettyChannelInitializer;
import cn.teaey.lostrpc.codec.Codec;
import cn.teaey.lostrpc.codec.ProtobufCodec;
import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaofei.wxf
 */
public class ProtobufInitializer extends NettyChannelInitializer {

    private final static Logger logger = LoggerFactory.getLogger(NettyChannelInitializer.class);
    private final ChannelHandler encoder;
    private final Codec codec;

    private ProtobufInitializer(Safety safety, Ctx ctx, MessageLite prototype,
        ChannelHandler... handlers) {
        super(safety, ctx, handlers);
        if (null == prototype) {
            throw new NullPointerException("prototype");
        }
        this.codec = new ProtobufCodec(prototype);
        this.encoder = FrameCodec.newEncoder(getSafety(), codec, ctx);
    }

    public static NettyChannelInitializer newInstance(Safety safety, Ctx ctx, MessageLite prototype,
        ChannelHandler... handler) {
        return new ProtobufInitializer(safety, ctx, prototype, handler);
    }

    @Override
    protected void decoders(Channel ch, Ctx ctx) throws Exception {
        ch.pipeline().addLast("decoder", FrameCodec.newDecoder(getSafety(), codec, ctx));
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
