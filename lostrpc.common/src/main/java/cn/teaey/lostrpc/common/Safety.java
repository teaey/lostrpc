package cn.teaey.lostrpc.common;

import cn.teaey.lostrpc.Cmd;
import cn.teaey.lostrpc.LostRpcException;
import cn.teaey.lostrpc.safety.AESEnAndDecryption;
import cn.teaey.lostrpc.safety.RSAEnAndDecryption;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiaofei.wxf
 */
public class Safety {
    public static Safety NOT_SAFETY_CLIENT = new Safety(false, false);
    public static Safety NOT_SAFETY_SERVER = new Safety(false, true);

    private final boolean isSafety;
    private final CountDownLatch authedCDL = new CountDownLatch(1);
    private final boolean isServer;
    private volatile boolean authed = false;
    private byte[] privateRsaKey = new byte[0];
    private byte[] publicRsaKey = new byte[0];
    private AESEnAndDecryption aes;
    private RSAEnAndDecryption rsa;
    private byte[] syncKeyPacketData = new byte[0];

    private Safety(boolean needSafety, boolean isServer) {
        this.isSafety = needSafety;
        if (!this.isSafety) {
            this.authed();
        }
        this.isServer = isServer;
    }

    public static Safety newServerSafety() {
        return new Safety(true, true);
    }

    public static Safety newClientSafety() {
        return new Safety(true, false);
    }

    public byte[] getPrivateRsaKey() {
        return privateRsaKey;
    }

    public void setPrivateRsaKey(byte[] privateRsaKey) {
        this.privateRsaKey = privateRsaKey;
    }

    public byte[] getPublicRsaKey() {
        return publicRsaKey;
    }

    void setPublicRsaKey(byte[] publicRsaKey) {
        this.publicRsaKey = publicRsaKey;
        ByteBuffer bb = ByteBuffer.allocate(5 + publicRsaKey.length);
        bb.putInt(publicRsaKey.length);
        bb.put(Cmd.CMD_SYNCKEY_RES.getType());
        bb.put(publicRsaKey);
        syncKeyPacketData = bb.array();
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

    public AESEnAndDecryption getAes() {
        return aes;
    }

    public void setAes(AESEnAndDecryption aes) {
        this.aes = aes;
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


    public static class UnauthException extends LostRpcException {
        public UnauthException() {
        }

        public UnauthException(String message) {
            super(message);
        }

        public UnauthException(String message, Throwable cause) {
            super(message, cause);
        }

        public UnauthException(Throwable cause) {
            super(cause);
        }

        public UnauthException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }


    public static class EncryptException extends LostRpcException {
        public EncryptException() {
        }

        public EncryptException(String message) {
            super(message);
        }

        public EncryptException(String message, Throwable cause) {
            super(message, cause);
        }

        public EncryptException(Throwable cause) {
            super(cause);
        }

        public EncryptException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }


    public static class DecryptException extends LostRpcException {
        public DecryptException() {
        }

        public DecryptException(String message) {
            super(message);
        }

        public DecryptException(String message, Throwable cause) {
            super(message, cause);
        }

        public DecryptException(Throwable cause) {
            super(cause);
        }

        public DecryptException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

}
