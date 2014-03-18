package com.taobao.teaey.lostrpc.safety;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author xiaofei.wxf
 */
public class RSAEnAndDecryption implements EnAndDecryption {

    //1024 117
    //2048 245

    private PublicKey publicKey;

    private PrivateKey privateKey;

    public Signature rsaSignature;

    public RSAEnAndDecryption() {

    }

    public static final String ALGORITHM = "RSA";
    public static final String AES_CIPHER = "AESEnAndDecryption/ECB/NoPadding";
    public static final String RSA_CIPHER = "RSA/ECB/PKCS1PADDING";
    public static final String RSA_SIGNATURE = "MD5withRSA";

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void loadPubkeyFromFile(String path) throws Exception {
        File file = new File(path);
        byte[] keyBytes = processKey(file);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(getAlgorithm());
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
        this.publicKey = publicK;

        //this.rsaSignature = Signature.getInstance(RSA_SIGNATURE);
        //this.rsaSignature.initVerify(publicKey);
    }

    public void loadPrivkeyFromFile(String path) throws Exception {
        File file = new File(path);
        byte[] keyBytes = processKey(file);
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(getAlgorithm());
        PrivateKey publicK = keyFactory.generatePrivate(pkcs8);
        this.privateKey = publicK;

        this.rsaSignature = Signature.getInstance(RSA_SIGNATURE);
        this.rsaSignature.initSign(privateKey);
    }

    /**
     * x509
     *
     * @param key base64 decoded
     * @throws Exception
     */
    public void loadPubkeyFromBytes(byte[] key) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(getAlgorithm());
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
        this.publicKey = publicK;
        this.rsaSignature = Signature.getInstance(RSA_SIGNATURE);
        this.rsaSignature.initVerify(publicKey);
    }

    /**
     * pkcs8
     *
     * @param key
     * @throws Exception
     */
    public void loadPrivkeyFromBytes(byte[] key) throws Exception {
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(getAlgorithm());
        PrivateKey publicK = keyFactory.generatePrivate(pkcs8);
        this.privateKey = publicK;
        this.rsaSignature = Signature.getInstance(RSA_SIGNATURE);
        this.rsaSignature.initSign(privateKey);
    }

    /**
     * 1 去除key的头跟尾 2 base64 decode
     *
     * @param file
     * @return
     * @throws Exception
     */
    byte[] processKey(File file) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while (((line = in.readLine()) != null)) {
            if ((line.charAt(0) != '-') && (line.charAt(line.length() - 1) != '-'))
                sb.append(line);
        }
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] ret = decoder.decodeBuffer(sb.toString());
        return ret;
    }

    @Override
    public byte[] encrypt(byte[] data) {
        try {
            Cipher rsa;
            rsa = Cipher.getInstance(RSA_CIPHER);
            rsa.init(Cipher.ENCRYPT_MODE, publicKey);
            return rsa.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        try {
            Cipher rsa;
            rsa = Cipher.getInstance(RSA_CIPHER);
            rsa.init(Cipher.DECRYPT_MODE, privateKey);
            return rsa.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getAlgorithm() {
        return ALGORITHM;
    }

    public byte[] sign(byte[] aesKey) {
        try {
            rsaSignature.update(aesKey);
            return rsaSignature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean verify(byte[] aesKey, byte[] sign) {
        try {
            rsaSignature.update(aesKey);
            return rsaSignature.verify(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
