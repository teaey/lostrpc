package com.taobao.teaey.lostrpc.codec;

/**
 * Encoder Interface
 *
 * @author xiaofei.wxf email:masfay@163.com
 */
public interface Encoder {

    /**
     * Encode Object to byte[]
     */
    byte[] encode(Object object) throws Exception;

}
