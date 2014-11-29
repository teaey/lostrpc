package cn.teaey.lostrpc.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author xiaofei.wxf email:masfay@163.com
 */
public class JavaCodec implements Codec {
    public static final JavaCodec INSTANCE = new JavaCodec();

    private JavaCodec() {
    }

    @Override
    public Object decode(byte[] bytes) throws Exception {
        ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Object resultObject = objectIn.readObject();
        objectIn.close();
        return resultObject;
    }

    @Override
    public byte[] encode(Object object) throws Exception {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteArray);
        output.writeObject(object);
        output.flush();
        output.close();
        return byteArray.toByteArray();
    }
}
