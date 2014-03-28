package com.taobao.teaey.lostrpc.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 编码Date子类，返回的是Date
 * 如果类添加字段，用老的类序列化的byte数组去反序列化新的类，会抛异常；删除字段，暂时测试可以
 *
 * @author xiaofei.wxf email:masfay@163.com
 */
public class KryoCodec implements Codec {
    private KryoCodec() {
    }

    public static final KryoCodec INSTANCE = new KryoCodec();
    private static final ThreadLocal<Kryo> KRYO_CACHE = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
            kryo.setReferences(false);
            return kryo;
        }
    };

    private Kryo getKryo() {
        return KRYO_CACHE.get();
    }


    @Override
    public Object decode(byte[] bytes) throws Exception {
        Input input = new Input(bytes);
        return getKryo().readClassAndObject(input);
    }

    @Override
    public byte[] encode(Object object) throws Exception {
        Output output = new Output(1024);
        getKryo().writeClassAndObject(output, object);
        return output.toBytes();
    }
}
