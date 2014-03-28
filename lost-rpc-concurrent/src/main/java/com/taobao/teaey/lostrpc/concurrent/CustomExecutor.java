package com.taobao.teaey.lostrpc.concurrent;

import java.util.concurrent.Executor;

/**
 * @author xiaofei.wxf
 */
public abstract class CustomExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        exec(command);
    }

    public abstract void exec(Runnable r);
}
