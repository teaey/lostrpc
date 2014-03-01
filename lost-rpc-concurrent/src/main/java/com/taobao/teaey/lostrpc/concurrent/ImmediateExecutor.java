package com.taobao.teaey.lostrpc.concurrent;

import java.util.concurrent.Executor;

/**
 * @author xiaofei.wxf
 */
public class ImmediateExecutor implements Executor {
    public static final ImmediateExecutor INSTANCE = new ImmediateExecutor();

    private ImmediateExecutor() {
        // use static instance
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        command.run();
    }
}