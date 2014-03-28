package com.taobao.teaey.lostrpc.codec;

/**
 * 目前使用Fastjson进行json序列化和反序列化
 *
 * @author xiaofei.wxf email:masfay@163.com
 */
public class JsonCodec implements Codec {
    public static final String NULL = "null";
    public static final byte[] NULL_BYTES = "null".getBytes(DEFAULT_CHARSET);
    public static final JsonCodec INSTANCE = new JsonCodec();

    private JsonCodec() {
    }

    @Override
    public Object decode(byte[] bytes) throws Exception {
        return com.alibaba.fastjson.JSON.parse(new String(bytes, DEFAULT_CHARSET));
    }

    /**
     * 默认只会对public字段进行编码
     *
     * @param obj
     * @return
     * @throws Exception
     */
    @Override
    public byte[] encode(Object obj) throws Exception {
        if (null == obj) {
            return NULL_BYTES;
        }
        String str = com.alibaba.fastjson.JSON.toJSONString(obj);
        return str.getBytes(DEFAULT_CHARSET);
    }

    public String encode2String(Object obj) {
        if (null == obj) {
            return NULL;
        }
        return com.alibaba.fastjson.JSON.toJSONString(obj);
    }
}

