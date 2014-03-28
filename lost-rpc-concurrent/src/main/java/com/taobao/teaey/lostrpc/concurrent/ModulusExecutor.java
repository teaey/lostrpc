package com.taobao.teaey.lostrpc.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author xiaofei.wxf
 */
public class ModulusExecutor extends CustomExecutor {
    public static ModulusExecutor newOne(int mod) {
        return new ModulusExecutor(mod);
    }

    private ModulusExecutor(int threadNum) {
        executors = new Executor[threadNum];
        mask = threadNum - 1;
        initThreads();
    }

    private final Executor[] executors;
    private final int mask;

    private void initThreads() {
        final int length = mask;
        for (int i = 0; i < length; i++) {
            executors[i] = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public void exec(Runnable r) {
        if (r instanceof IdentityTask) {
            final long id = ((IdentityTask) r).id();
            int idx = (int) (id % mask);
            executors[idx].execute(r);
        } else {
            throw new RuntimeException(
                "ModulusExecutor unsupport this Runnable type:" + r.getClass().getName());
        }
    }
}
