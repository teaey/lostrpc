package com.taobao.teaey.lostrpc.codec;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author xiaofei.wxf email:masfay@163.com
 */
public class Hessian2Codec implements Codec {
    private final SerializerFactory factory;

    private Hessian2Codec(SerializerFactory factory) {
        this.factory = factory;
    }

    public static final Hessian2Codec INSTANCE =
        new Hessian2Codec(new SerializerFactory(Codec.class.getClassLoader()));

    @Override
    public Object decode(byte[] bytes) throws Exception {
        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
        input.setSerializerFactory(factory);
        // avoid child object to parent object problem
        Object resultObject = input.readObject();
        input.close();
        return resultObject;
    }

    @Override
    public byte[] encode(Object object) throws Exception {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(byteArray);
        output.setSerializerFactory(factory);
        output.writeObject(object);
        output.close();
        return byteArray.toByteArray();
    }
}
