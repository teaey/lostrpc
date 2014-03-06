package com.taobao.teaey.lostrpc.common;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    public static class SyncExecutor extends CustomExecutor<CustomDispatcher.CustomTask> {
        @Override
        public void exec(CustomDispatcher.CustomTask customTask) {
            customTask.run();
        }
    }

    public static class AsyncExecutor extends CustomExecutor<CustomDispatcher.CustomTask> {
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

    public static class ModulusExecutor extends CustomExecutor<CustomDispatcher.CustomTask> {
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
            final int id = t.c.getId();
            int idx = id % mask;
            executors[idx].execute(t);
        }
    }
}
