package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.concurrent.MultiThreadExecutor;
import com.taobao.teaey.lostrpc.concurrent.SingleThreadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author xiaofei.wxf
 */
public abstract class AsyncDispatcher<Channel, MsgType> implements Dispatcher<Channel, MsgType> {
    private static final Logger logger = LoggerFactory.getLogger(AsyncDispatcher.class);

    public AsyncDispatcher() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public AsyncDispatcher(Executor executor) {
        this.executor = executor;
    }

    public AsyncDispatcher(int poolSize) {
        this(poolSize <= 1 ? new SingleThreadExecutor() : new MultiThreadExecutor(poolSize));
    }

    private final Executor executor;

    public class AsyncTask implements Task {
        AsyncTask(Channel c, MsgType p) {
            this.c = c;
            this.p = p;
        }

        Channel c;
        MsgType p;

        @Override
        public void run() {
            try {
                asyncDispatch(c, p);
            } catch (Exception e) {
                logger.error("消息处理出错:\n", e);
            }
        }
    }

    public abstract void asyncDispatch(Channel c, MsgType m) throws Exception;

    @Override
    public void dispatch(Channel c, MsgType p) {
        executor.execute(new AsyncTask(c, p));
    }
}
