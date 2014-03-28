package com.taobao.teaey.lostrpc.common;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.taobao.teaey.lostrpc.NettyChannelInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.UnsupportedDataTypeException;

/**
 * @author xiaofei.wxf
 */
public class ProtobufInitializer extends NettyChannelInitializer {

    private final static Logger logger = LoggerFactory.getLogger(NettyChannelInitializer.class);


    public static NettyChannelInitializer newInstance(Safety safety, MessageLite prototype,
        ChannelHandler... handler) {
        return new ProtobufInitializer(safety, prototype, handler);
    }

    private static final boolean HAS_PARSER;

    static {
        boolean hasParser = false;
        try {
            // MessageLite.getParsetForType() is not available until protobuf 2.5.0.
            MessageLite.class.getDeclaredMethod("getParserForType");
            hasParser = true;
        } catch (Throwable t) {
            // Ignore
        }

        HAS_PARSER = hasParser;
    }


    private class Encoder extends PacketFrameEncoder {
        public Encoder(Safety safety) {
            super(safety);
        }

        @Override
        public byte[] onMsg(Object msg) throws UnsupportedDataTypeException {
            byte[] body;
            if (msg instanceof MessageLite) {
                body = ((MessageLite) msg).toByteArray();
            } else if (msg instanceof MessageLite.Builder) {
                body = ((MessageLite.Builder) msg).build().toByteArray();
            } else {
                throw new UnsupportedDataTypeException(msg.getClass().getName());
            }
            return body;
        }
    }


    private class Decoder extends PacketFrameDecoder {

        private final MessageLite prototype;
        private final ExtensionRegistry extensionRegistry;

        /**
         * Creates a new instance.
         */
        public Decoder(Safety safety, MessageLite prototype) {
            this(safety, prototype, null);
        }

        public Decoder(Safety safety, MessageLite prototype, ExtensionRegistry extensionRegistry) {
            super(safety);
            if (prototype == null) {
                throw new NullPointerException("prototype");
            }
            this.prototype = prototype.getDefaultInstanceForType();
            this.extensionRegistry = extensionRegistry;
        }

        @Override
        public Object onData(byte[] body) throws InvalidProtocolBufferException {
            Object ret = null;
            if (extensionRegistry == null) {
                if (HAS_PARSER) {
                    ret = prototype.getParserForType().parseFrom(body);
                } else {
                    ret = prototype.newBuilderForType().mergeFrom(body).build();
                }
            } else {
                if (HAS_PARSER) {
                    ret = prototype.getParserForType().parseFrom(body, extensionRegistry);
                } else {
                    ret = prototype.newBuilderForType().mergeFrom(body, extensionRegistry).build();
                }
            }
            return ret;
        }
    }


    private final MessageLite defaultIns;

    private final ChannelHandler encoder;


    private ProtobufInitializer(Safety safety, MessageLite prototype, ChannelHandler... handlers) {
        super(safety, handlers);
        if (null == prototype) {
            throw new NullPointerException("prototype");
        }
        this.defaultIns = prototype;
        this.encoder = new Encoder(safety);
    }


    @Override
    protected void decoders(Channel ch) throws Exception {
        ch.pipeline().addLast("decoder", new Decoder(getSafety(), this.defaultIns));
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
