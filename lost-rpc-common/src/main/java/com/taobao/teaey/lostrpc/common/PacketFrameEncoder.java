package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.Cmd;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;

/**
 * @author xiaofei.wxf
 */
@ChannelHandler.Sharable
public abstract class PacketFrameEncoder extends MessageToByteEncoder<Object> {
    private final Safety safety;

    public PacketFrameEncoder(Safety safety) {
        this.safety = safety;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, java.lang.Object msg, ByteBuf out)
        throws Exception {
        if (msg instanceof ByteBuf) {
            out.writeBytes((ByteBuf) msg);
        } else if (msg instanceof byte[]) {
            out.writeBytes((byte[]) msg);
        } else if (msg instanceof ByteBuffer) {
            out.writeBytes((ByteBuffer) msg);
        } else {
            byte[] body = onMsg(msg);
            if (safety.isSafety()) {
                try {
                    body = safety.getAes().encrypt(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int bodyLen = body.length;
            out.ensureWritable(5 + bodyLen);
            out.writeInt(bodyLen);
            out.writeByte(Cmd.CMD_DATA.getType());
            out.writeBytes(body);
        }
    }

    public abstract byte[] onMsg(Object msg) throws Exception;
}
