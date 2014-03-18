package com.taobao.teaey.lostrpc.safety;

/**
 * @author xiaofei.wxf
 */
public interface EnAndDecryption {
    byte[] encrypt(byte[] data) throws Exception;

    byte[] decrypt(byte[] data) throws Exception;

    String getAlgorithm();
}
