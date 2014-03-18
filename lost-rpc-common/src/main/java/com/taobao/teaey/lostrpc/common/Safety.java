package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.Cmd;
import com.taobao.teaey.lostrpc.safety.AESEnAndDecryption;
import com.taobao.teaey.lostrpc.safety.RSAEnAndDecryption;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xiaofei.wxf
 */
public class Safety {
    public static Safety NOT_SAFETY_CLIENT = new Safety(false, false);
    public static Safety NOT_SAFETY_SERVER = new Safety(false, true);
    private final boolean isSafety;
    private final AtomicBoolean authed = new AtomicBoolean(false);

    private byte[] privateRsaKey = new byte[0];
    private byte[] publicRsaKey = new byte[0];

    private AESEnAndDecryption aes;
    private RSAEnAndDecryption rsa;
    private byte[] syncKeyPacketData = new byte[0];

    public byte[] getPrivateRsaKey() {
        return privateRsaKey;
    }

    public void setPrivateRsaKey(byte[] privateRsaKey) {
        this.privateRsaKey = privateRsaKey;
    }

    public byte[] getPublicRsaKey() {
        return publicRsaKey;
    }

    public RSAEnAndDecryption getRsa() {
        return rsa;
    }

    public void setRsa(RSAEnAndDecryption rsa) {
        this.rsa = rsa;
        byte[] key = rsa.getPublicKey().getEncoded();
        setPublicRsaKey(key);
    }

    public byte[] getSyncKeyPacketData() {
        return syncKeyPacketData;
    }

    public void setSyncKeyPacketData(byte[] syncKeyPacketData) {
        this.syncKeyPacketData = syncKeyPacketData;
    }

    void setPublicRsaKey(byte[] publicRsaKey) {
        this.publicRsaKey = publicRsaKey;
        ByteBuffer bb = ByteBuffer.allocate(5 + publicRsaKey.length);
        bb.putInt(publicRsaKey.length);
        bb.put(Cmd.CMD_SYNCKEY_RES.getType());
        bb.put(publicRsaKey);
        syncKeyPacketData = bb.array();
    }

    public AESEnAndDecryption getAes() {
        return aes;
    }

    public void setAes(AESEnAndDecryption aes) {
        this.aes = aes;
    }

    public static class UnauthException extends RuntimeException {
        public UnauthException(String msg) {
            super(msg);
        }
    }

    public static Safety newServerSafety() {
        return new Safety(true, true);
    }

    public static Safety newClientSafety() {
        return new Safety(true, false);
    }

    private final boolean isServer;

    private Safety(boolean needSafety, boolean isServer) {
        this.isSafety = needSafety;
        this.isServer = isServer;
    }

    public boolean isAuthed() {
        return authed.get();
    }

    public void authed() {
        authed.compareAndSet(false, true);
    }

    public boolean isSafety() {
        return isSafety;
    }

    public boolean isServer() {
        return isServer;
    }

}
