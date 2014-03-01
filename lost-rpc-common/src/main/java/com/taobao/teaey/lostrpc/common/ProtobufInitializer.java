package com.taobao.teaey.lostrpc.common;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.MessageLite;
import com.taobao.teaey.lostrpc.NettyChannelInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.UnsupportedDataTypeException;
import java.util.List;

/**
 * @author xiaofei.wxf
 */
public class ProtobufInitializer extends NettyChannelInitializer {

    private final static Logger logger = LoggerFactory.getLogger(NettyChannelInitializer.class);

    /**
     * PROTOCOL BUFFERS *
     */
    public static NettyChannelInitializer newInstance(MessageLite prototype, ChannelHandler... handler) {
        return new ProtobufInitializer(prototype, handler);
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

    @ChannelHandler.Sharable
    private class Encoder extends MessageToByteEncoder<Object> {
        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            byte[] body;
            if (msg instanceof MessageLite) {
                body = ((MessageLite) msg).toByteArray();
            } else if (msg instanceof MessageLite.Builder) {
                body = ((MessageLite.Builder) msg).build().toByteArray();
            } else {
                throw new UnsupportedDataTypeException(msg.getClass().getName());
            }
            int bodyLen = body.length;
            out.ensureWritable(4 + bodyLen);
            out.writeInt(bodyLen);
            out.writeBytes(body);
        }
    }

    private class Decoder extends ByteToMessageDecoder {


        private final MessageLite prototype;
        private final ExtensionRegistry extensionRegistry;

        /**
         * Creates a new instance.
         */
        public Decoder(MessageLite prototype) {
            this(prototype, null);
        }

        public Decoder(MessageLite prototype, ExtensionRegistry extensionRegistry) {
            if (prototype == null) {
                throw new NullPointerException("prototype");
            }
            this.prototype = prototype.getDefaultInstanceForType();
            this.extensionRegistry = extensionRegistry;
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

            if (extensionRegistry == null) {
                if (HAS_PARSER) {
                    out.add(prototype.getParserForType().parseFrom(body));
                } else {
                    out.add(prototype.newBuilderForType().mergeFrom(body).build());
                }
            } else {
                if (HAS_PARSER) {
                    out.add(prototype.getParserForType().parseFrom(body, extensionRegistry));
                } else {
                    out.add(prototype.newBuilderForType().mergeFrom(body, extensionRegistry).build());
                }
            }
        }
    }

    private final MessageLite defaultIns;

    private final ChannelHandler encoder = new Encoder();

    private ProtobufInitializer(MessageLite prototype, ChannelHandler... handlers) {
        super(handlers);
        if (null == prototype) {
            throw new NullPointerException("prototype");
        }
        this.defaultIns = prototype;
    }

    @Override
    protected void decoders(Channel ch) throws Exception {
        ch.pipeline().addLast("decoder", new Decoder(this.defaultIns));
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
