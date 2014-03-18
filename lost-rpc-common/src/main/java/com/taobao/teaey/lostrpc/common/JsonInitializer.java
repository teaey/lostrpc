package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.NettyChannelInitializer;
import com.taobao.teaey.lostrpc.codec.JsonCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xiaofei.wxf
 */
public class JsonInitializer extends NettyChannelInitializer {

    private final static Logger logger = LoggerFactory.getLogger(NettyChannelInitializer.class);

    public static NettyChannelInitializer newInstance(Safety safety, ChannelHandler... handler) {
        return new JsonInitializer(safety, handler);
    }

    private class Encoder extends PacketFrameEncoder {
        public Encoder(Safety safety) {
            super(safety);
        }

        @Override
        public byte[] onMsg(Object msg) throws Exception {
            return JsonCodec.INSTANCE.encode(msg);
        }
    }

    private class Decoder extends PacketFrameDecoder {
        public Decoder(Safety safety) {
            super(safety);
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            if (in.readableBytes() <= 4) return;

            in.markReaderIndex();

            int bodyLen = in.readInt();

            if (in.readableBytes() < bodyLen) {
                in.resetReaderIndex();
                return;
            }

            byte[] body = new byte[bodyLen];

            in.readBytes(body);

            out.add(JsonCodec.INSTANCE.decode(body));
        }

        @Override
        public Object onData(byte[] body) throws Exception {
            return JsonCodec.INSTANCE.decode(body);
        }
    }

    private final ChannelHandler encoder;

    private final Safety safety;

    private JsonInitializer(Safety safety, ChannelHandler... handlers) {
        super(handlers);
        this.safety = safety;
        this.encoder = new Encoder(safety);
    }

    @Override
    protected void decoders(Channel ch) throws Exception {
        ch.pipeline().addLast("decoder", new Decoder(this.safety));
    }

    @Override
    protected void encoders(Channel ch) throws Exception {
        ch.pipeline().addLast("encoder", encoder);
    }

    @Override
    protected void handlers(Channel ch) throws Exception {
        for (int i = 1; i <= handlers.length; i++) {
            ch.pipeline().addLast("handler-" + i, handlers[i - 1]);
        }
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
