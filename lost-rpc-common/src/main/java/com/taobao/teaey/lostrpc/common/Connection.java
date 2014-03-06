package com.taobao.teaey.lostrpc.common;

/**
 * @author xiaofei.wxf
 */
public interface Connection {
    int getId();
    void writeAndFlush(Object msg);
}
