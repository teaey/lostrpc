package cn.teaey.lostrpc.common;

import sun.misc.BASE64Decoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author xiaofei.wxf on 14-2-13.
 */
public class LostUtils {
    public static byte[] processKeyStore(InputStream is) throws IOException {
        if (null == is) {
            throw new NullPointerException("input stream");
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while (((line = in.readLine()) != null)) {
            if ((line.charAt(0) != '-') && (line.charAt(line.length() - 1) != '-')) {
                sb.append(line);
            }
        }
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(sb.toString());
    }
}
