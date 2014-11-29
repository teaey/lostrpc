package cn.teaey.lostrpc.codec;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.MessageLite;

import javax.activation.UnsupportedDataTypeException;

/**
 * Created by xiaofei.wxf on 2014/11/29.
 */
public class ProtobufCodec implements Codec {
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

    private final MessageLite prototype;
    private final ExtensionRegistry extensionRegistry;

    public ProtobufCodec(MessageLite prototype,
        ExtensionRegistry extensionRegistry) {
        this.prototype = prototype;
        this.extensionRegistry = extensionRegistry;
    }

    public ProtobufCodec(MessageLite prototype) {
        this(prototype, null);
    }

    @Override public Object decode(byte[] bytes) throws Exception {
        Object ret = null;
        if (extensionRegistry == null) {
            if (HAS_PARSER) {
                ret = prototype.getParserForType().parseFrom(bytes);
            } else {
                ret = prototype.newBuilderForType().mergeFrom(bytes).build();
            }
        } else {
            if (HAS_PARSER) {
                ret = prototype.getParserForType().parseFrom(bytes, extensionRegistry);
            } else {
                ret = prototype.newBuilderForType().mergeFrom(bytes, extensionRegistry).build();
            }
        }
        return ret;
    }

    @Override public byte[] encode(Object msg) throws Exception {
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
