package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.NettyChannelInitializer;
import com.taobao.teaey.lostrpc.codec.JsonCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xiaofei.wxf
 */
public class JsonInitializer extends NettyChannelInitializer {

    private final static Logger logger = LoggerFactory.getLogger(NettyChannelInitializer.class);

    /**
     * JSON
     */

    public static NettyChannelInitializer newInstance(ChannelHandler... handler) {
        return new JsonInitializer(handler);
    }

    @Sharable
    private class Encoder extends MessageToByteEncoder<Object> {
        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            byte[] body = JsonCodec.INSTANCE.encode(msg);
            int bodyLen = body.length;
            out.ensureWritable(4 + bodyLen);
            out.writeInt(bodyLen);
            out.writeBytes(body);
        }
    }

    private class Decoder extends ByteToMessageDecoder {

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
    }

    private final ChannelHandler encoder = new Encoder();

    private JsonInitializer(ChannelHandler... handlers) {
        super(handlers);
    }

    @Override
    protected void decoders(Channel ch) throws Exception {
        ch.pipeline().addLast("decoder", new Decoder());
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
