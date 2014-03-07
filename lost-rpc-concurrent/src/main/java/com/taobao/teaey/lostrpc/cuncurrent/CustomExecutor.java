package com.taobao.teaey.lostrpc.cuncurrent;

import java.util.concurrent.Executor;

/**
 * @author xiaofei.wxf
 */
public abstract class CustomExecutor<T extends Runnable> implements Executor {
    @Override
    public void execute(Runnable command) {
        T t = (T) command;
        exec(t);
    }

    public abstract void exec(T t);
}
