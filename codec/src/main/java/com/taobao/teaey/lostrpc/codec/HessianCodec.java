package com.taobao.teaey.lostrpc.codec;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author xiaofei.wxf email:masfay@163.com
 */
public class HessianCodec implements Codec {
    public static final HessianCodec INSTANCE = new HessianCodec(new SerializerFactory());
    private final SerializerFactory serializerFactory;

    private HessianCodec(final SerializerFactory serializerFactory) {
        this.serializerFactory = serializerFactory;
    }

    @Override
    public Object decode(byte[] bytes) throws Exception {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(HessianCodec.class.getClassLoader());
        HessianInput input = new HessianInput(new ByteArrayInputStream(bytes));
        input.setSerializerFactory(serializerFactory);
        Object resultObject = input.readObject();
        Thread.currentThread().setContextClassLoader(tccl);
        return resultObject;
    }

    @Override
    public byte[] encode(Object object) throws Exception {
        ByteArrayOutputStream binary = new ByteArrayOutputStream();
        HessianOutput hout = new HessianOutput(binary);
        hout.setSerializerFactory(serializerFactory);
        hout.writeObject(object);
        return binary.toByteArray();
    }
}
