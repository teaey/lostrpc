package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.NettyChannelInitializer;
import com.taobao.teaey.lostrpc.codec.JsonCodec;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        public Object onData(byte[] body) throws Exception {
            return JsonCodec.INSTANCE.decode(body);
        }
    }


    private final ChannelHandler encoder;

    private JsonInitializer(Safety safety, ChannelHandler... handlers) {
        super(safety, handlers);
        this.encoder = new Encoder(safety);
    }

    @Override
    protected void decoders(Channel ch) throws Exception {
        ch.pipeline().addLast("decoder", new Decoder(getSafety()));
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
