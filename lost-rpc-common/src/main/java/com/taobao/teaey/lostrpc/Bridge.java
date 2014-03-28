package com.taobao.teaey.lostrpc;

/**
 * @author xiaofei.wxf
 */
public interface Bridge<A, B> {
    void bridge(A a, B b);
}
