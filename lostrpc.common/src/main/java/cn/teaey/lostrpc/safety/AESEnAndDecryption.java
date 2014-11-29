package cn.teaey.lostrpc.safety;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaofei.wxf
 */
public class AESEnAndDecryption implements EnAndDecryption {

    public static final byte PADDING = '{';
    public static final int BLOCK_SIZE = 16;

    public static final String ALGORITHM = "AES";
    public static final String AES_CIPHER = "AES/ECB/NoPadding";

    private SecretKeySpec aesKeySpec;

    private Cipher aesEncryptKey;
    private Cipher aesDecryptKey;

    private byte[] keyBytes;

    public AESEnAndDecryption(byte[] key) {
        this.aesKeySpec = new SecretKeySpec(key, getAlgorithm());
        this.keyBytes = key;
        try {
            init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] newKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(keySize);
        SecretKey secretKey = keyGen.generateKey();
        return secretKey.getEncoded();
    }

    public static byte[] newKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
            return secretKey.getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void init() throws Exception {
        this.aesDecryptKey = Cipher.getInstance(AES_CIPHER);
        this.aesDecryptKey.init(Cipher.DECRYPT_MODE, aesKeySpec);

        this.aesEncryptKey = Cipher.getInstance(AES_CIPHER);
        this.aesEncryptKey.init(Cipher.ENCRYPT_MODE, aesKeySpec);
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {

        int padCount = (BLOCK_SIZE - (data.length + 1) % BLOCK_SIZE) % BLOCK_SIZE;
        byte[] enData = new byte[data.length + padCount + 1];

        enData[0] = (byte) padCount;
        System.arraycopy(data, 0, enData, 1, data.length);

        for (int i = 1 + data.length; i < enData.length; i++) {
            enData[i] = PADDING;
        }

        return aesEncryptKey.doFinal(enData);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        byte[] deData = this.aesDecryptKey.doFinal(data);
        int padCount = deData[0];
        int origDataLength = deData.length - 1 - padCount;
        byte[] outData = new byte[origDataLength];
        System.arraycopy(deData, 1, outData, 0, origDataLength);
        return outData;
    }

    @Override
    public String getAlgorithm() {
        return ALGORITHM;
    }


    public byte[] getKeyBytes() {
        return this.keyBytes;
    }
}
