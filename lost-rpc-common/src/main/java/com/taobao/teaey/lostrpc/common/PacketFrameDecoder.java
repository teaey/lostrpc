package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.Cmd;
import com.taobao.teaey.lostrpc.safety.AESEnAndDecryption;
import com.taobao.teaey.lostrpc.safety.RSAEnAndDecryption;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author xiaofei.wxf
 */
public abstract class PacketFrameDecoder extends ByteToMessageDecoder {
    private final Safety safety;

    public PacketFrameDecoder(Safety safety) {
        this.safety = safety;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
//        if (!safety.isServer()) {
//            ByteBuf buf = Unpooled.buffer(5);
//            buf.writeInt(0);
//            buf.writeByte(Cmd.CMD_SYNCKEY_REQ.getType());
//            ctx.writeAndFlush(buf);
//        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 5) return;

        in.markReaderIndex();

        int bodyLen = in.readInt();

        int cmdType = in.readByte();

        if (in.readableBytes() < bodyLen) {
            in.resetReaderIndex();
            return;
        }

        byte[] body = new byte[bodyLen];

        in.readBytes(body);

        Cmd cmd = Cmd.match(cmdType);

        switch (cmd) {
            case CMD_DATA:
                if (safety.isSafety() && !safety.isAuthed()) {
                    throw new Safety.UnauthException("unauth");
                }
                if (safety.isSafety()) {
                    body = safety.getAes().decrypt(body);
                }
                Object o = onData(body);
                if (null != o) {
                    out.add(o);
                }
                break;
            case CMD_SYNCKEY_REQ:
                onSyncKeyReq(body, ctx);
                break;
            case CMD_SYNCKEY_RES:
                onSyncKeyRes(body, ctx);
                break;
            case CMD_HANDSHAKE_REQ:
                onHandshakeReq(body, ctx);
                break;
            case CMD_HANDSHAKE_RES:
                onHandshakeRes(body, ctx);
                break;
        }
    }

    public void onSyncKeyRes(byte[] body, ChannelHandlerContext ctx) {
        RSAEnAndDecryption rsa = new RSAEnAndDecryption();
        try {
            rsa.loadPubkeyFromBytes(body);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        safety.setRsa(rsa);
        //创建aesKey
        safety.setAes(new AESEnAndDecryption(AESEnAndDecryption.newKey()));
        //握手
        byte[] hsBody = safety.getRsa().encrypt(safety.getAes().getKeyBytes());

        ByteBuf buf = Unpooled.buffer(5 + hsBody.length);
        buf.writeInt(hsBody.length);
        buf.writeByte(Cmd.CMD_HANDSHAKE_REQ.getType());
        buf.writeBytes(hsBody);
        ctx.writeAndFlush(buf);
    }

    public void onSyncKeyReq(byte[] body, ChannelHandlerContext ctx) {
        //TODO:比较版本
        ctx.writeAndFlush(Unpooled.wrappedBuffer(safety.getSyncKeyPacketData()));
    }

    public abstract Object onData(byte[] body) throws Exception;

    public void onHandshakeReq(byte[] body, ChannelHandlerContext ctx) {
        body = safety.getRsa().decrypt(body);
        AESEnAndDecryption aes = new AESEnAndDecryption(body);
        safety.setAes(aes);

        byte[] sign = safety.getRsa().sign(aes.getKeyBytes());
        ByteBuf buf = Unpooled.buffer(5 + sign.length);

        buf.writeInt(sign.length);
        buf.writeByte(Cmd.CMD_HANDSHAKE_RES.getType());
        buf.writeBytes(sign);
        ctx.writeAndFlush(buf);
        safety.authed();
    }

    public void onHandshakeRes(byte[] body, ChannelHandlerContext ctx) {
        boolean authed = safety.getRsa().verify(safety.getAes().getKeyBytes(), body);
        if (!authed) {
            throw new Safety.UnauthException("auth failed");
        }
        safety.authed();
    }

}
