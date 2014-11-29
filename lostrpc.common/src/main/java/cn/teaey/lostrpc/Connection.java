package cn.teaey.lostrpc;

/**
 * @author xiaofei.wxf
 */
public interface Connection {
    long getId();

    void writeAndFlush(Object msg);
}
