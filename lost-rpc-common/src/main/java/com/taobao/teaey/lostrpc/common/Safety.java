package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.Cmd;
import com.taobao.teaey.lostrpc.safety.AESEnAndDecryption;
import com.taobao.teaey.lostrpc.safety.RSAEnAndDecryption;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiaofei.wxf
 */
public class Safety {
    public static Safety NOT_SAFETY_CLIENT = new Safety(false, false);
    public static Safety NOT_SAFETY_SERVER = new Safety(false, true);

    private final boolean isSafety;
    private volatile boolean authed = false;

    private final CountDownLatch authedCDL = new CountDownLatch(1);

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


    public static class DecryptException extends RuntimeException {
        public DecryptException(String msg) {
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
        if (!this.isSafety) {
            this.authed();
        }
        this.isServer = isServer;
    }

    public boolean isAuthed() {
        return authed;
    }

    public void authed() {
        authed = true;
        authedCDL.countDown();
    }

    public void syncAuthed() {
        try {
            authedCDL.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean isSafety() {
        return isSafety;
    }

    public boolean isServer() {
        return isServer;
    }

}
