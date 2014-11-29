package cn.teaey.lostrpc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaofei.wxf on 2014/11/29.
 */
public class Ctx {
    private final Map<Object, Object> holder = new HashMap<Object, Object>();

    public Ctx copy() {
        Ctx to = new Ctx();
        to.holder.putAll(holder);
        return to;
    }

    @Override public String toString() {
        return "{" + holder.toString() + "}";
    }

    public Object get(Object key) {
        return holder.get(key);
    }

    public Object set(Object key, Object value) {
        return holder.put(key, value);
    }
}
