package com.taobao.teaey.lostrpc.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiaofei.wxf
 */
public class MultiThreadExecutor implements Executor {
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public MultiThreadExecutor() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public MultiThreadExecutor(int poolSize) {
        executor = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }
}
