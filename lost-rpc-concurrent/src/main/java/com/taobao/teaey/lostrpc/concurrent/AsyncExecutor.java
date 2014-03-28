package com.taobao.teaey.lostrpc.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author xiaofei.wxf
 */
public class AsyncExecutor extends CustomExecutor {
    public static AsyncExecutor newOne(Executor executor) {
        return new AsyncExecutor(executor);
    }

    public static AsyncExecutor newOne(int threadNum) {
        return new AsyncExecutor(Executors.newFixedThreadPool(threadNum));
    }

    public static AsyncExecutor newOne() {
        return new AsyncExecutor(Executors.newCachedThreadPool());
    }

    private final Executor executor;

    private AsyncExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void exec(Runnable t) {
        executor.execute(t);
    }
}
