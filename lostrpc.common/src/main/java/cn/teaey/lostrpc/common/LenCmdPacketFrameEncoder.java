package cn.teaey.lostrpc.common;

import cn.teaey.lostrpc.Cmd;
import cn.teaey.lostrpc.codec.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;

/**
 * @author xiaofei.wxf
 */
@ChannelHandler.Sharable
public class LenCmdPacketFrameEncoder extends MessageToByteEncoder<Object> {
    public static final String TYPE = "len-cmd";
    private final Safety safety;
    private final Codec codec;

    public LenCmdPacketFrameEncoder(Safety safety, Codec codec) {
        this.safety = safety;
        this.codec = codec;
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
            byte[] body = codec.encode(msg);
            if (safety.isSafety()) {
                try {
                    body = safety.getAes().encrypt(body);
                } catch (Exception e) {
                    throw new Safety.EncryptException("Encrypt failed:", e);
                }
            }
            int bodyLen = body.length;
            out.ensureWritable(5 + bodyLen);
            out.writeInt(bodyLen);
            out.writeByte(Cmd.CMD_DATA.getType());
            out.writeBytes(body);
        }
    }
}
