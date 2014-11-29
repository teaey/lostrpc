package cn.teaey.lostrpc.common;

import cn.teaey.lostrpc.Ctx;
import cn.teaey.lostrpc.LostRpcException;
import cn.teaey.lostrpc.codec.Codec;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by xiaofei.wxf on 2014/11/29.
 */
public class FrameCodec {
    public static final String TYPE = "len-cmd";

    public static ByteToMessageDecoder newDecoder(Safety safety, Codec codec, Ctx ctx) {
        String frameCodec = (String) ctx.get("frameCodec");
        if (null == frameCodec || frameCodec.length() == 0 || frameCodec
            .equals(TYPE)) {
            return new LenCmdPacketFrameDecoder(safety, codec);
        } else {
            throw new LostRpcException("Unknown FrameCodec type:" + frameCodec);
        }
    }

    public static MessageToByteEncoder newEncoder(Safety safety, Codec codec, Ctx ctx){
        String frameCodec = (String) ctx.get("frameCodec");
        if (null == frameCodec || frameCodec.length() == 0 || frameCodec
            .equals(TYPE)) {
            return new LenCmdPacketFrameEncoder(safety, codec);
        } else {
            throw new LostRpcException("Unknown FrameCodec type:" + frameCodec);
        }
    }
}
