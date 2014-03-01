package com.taobao.teaey.lostrpc.codec;

/**
 * Decoder Interface
 * @author xiaofei.wxf email:masfay@163.com
 */
public interface Decoder {

    /**
     * decode byte[] to Object
     */
    Object decode(byte[] bytes) throws Exception;

}
