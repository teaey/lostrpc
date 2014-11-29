package cn.teaey.lostrpc.server.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaofei.wxf
 */
public class Service {
    private final String _name;
    private final Class _interface;
    private final Object _instance;
    private final Map<String, Method> _methods = new HashMap<String, Method>(32);

    public Service(String name, Class anInterface, Object instance) {
        _name = name;
        _interface = anInterface;
        _instance = instance;
        for (Method m : anInterface.getMethods()) {
            _methods.put(m.getName(), m);
        }
    }

    public Object invoke(Method m, Object[] args)
        throws Exception {
        return m.invoke(_instance, args);
    }
}
