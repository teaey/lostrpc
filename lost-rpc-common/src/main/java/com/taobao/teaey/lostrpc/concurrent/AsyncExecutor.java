package com.taobao.teaey.lostrpc.concurrent;

import com.taobao.teaey.lostrpc.common.CustomDispatcher;
import com.taobao.teaey.lostrpc.cuncurrent.CustomExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author xiaofei.wxf
 */
public class AsyncExecutor extends CustomExecutor<CustomDispatcher.CustomTask> {
    private final Executor executor;

    public AsyncExecutor(int threadNum) {
        executor = Executors.newFixedThreadPool(threadNum);
    }

    public AsyncExecutor() {
        this(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void exec(CustomDispatcher.CustomTask t) {
        executor.execute(t);
    }
}
