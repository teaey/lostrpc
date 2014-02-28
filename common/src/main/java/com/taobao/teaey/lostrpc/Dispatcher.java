package com.taobao.teaey.lostrpc;

/**
 * Created by xiaofei.wxf on 14-2-13.
 */
public interface Dispatcher<C, T> {
    void dispatch(C c, T p);

    interface Task extends Runnable {
        @Override
        void run();
    }
}
