package cn.teaey.lostrpc;

/**
 * @author xiaofei.wxf
 */
public interface RpcSession {
    long connectionId();

    long sessionId();

    boolean close();
}
