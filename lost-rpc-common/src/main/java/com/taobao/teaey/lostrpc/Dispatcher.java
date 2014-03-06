package com.taobao.teaey.lostrpc;

import com.taobao.teaey.lostrpc.common.Connection;

/**
 * Created by xiaofei.wxf on 14-2-13.
 */
public interface Dispatcher<T> {
    void dispatch(Connection c, T p);

    interface Task extends Runnable {
        @Override
        void run();
    }
}
