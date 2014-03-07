package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.concurrent.AsyncExecutor;

/**
 * Created by XiaoFei on 14-3-6.
 */
public abstract class AsyncDispatcher<MsgType> extends CustomDispatcher<MsgType> {
    public AsyncDispatcher(int threadNum) {
        super(new AsyncExecutor(threadNum));
    }

    public AsyncDispatcher() {
        this(Runtime.getRuntime().availableProcessors());
    }
}
