package com.taobao.teaey.lostrpc.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiaofei.wxf
 */
public class SingleThreadExecutor implements Executor {
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }
}
