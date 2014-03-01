package com.taobao.teaey.lostrpc.codec;

import java.nio.charset.Charset;

/**
 * @author xiaofei.wxf email:masfay@163.com
 */
public interface Codec extends Decoder, Encoder {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    @Override
    Object decode(byte[] bytes) throws Exception;

    @Override
    byte[] encode(Object object) throws Exception;
}
