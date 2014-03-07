package com.taobao.teaey.lostrpc.concurrent;

import com.taobao.teaey.lostrpc.common.CustomDispatcher;
import com.taobao.teaey.lostrpc.cuncurrent.CustomExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author xiaofei.wxf
 */
public class ModulusExecutor extends CustomExecutor<CustomDispatcher.CustomTask> {
    public ModulusExecutor(int threadNum) {
        executors = new Executor[threadNum];
        mask = threadNum - 1;
        initThreads();
    }

    final Executor[] executors;
    final int mask;

    void initThreads() {
        final int length = mask;
        for (int i = 0; i < length; i++) {
            executors[i] = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public void exec(CustomDispatcher.CustomTask t) {
        final int id = t.getC().getId();
        int idx = id % mask;
        executors[idx].execute(t);
    }
}
