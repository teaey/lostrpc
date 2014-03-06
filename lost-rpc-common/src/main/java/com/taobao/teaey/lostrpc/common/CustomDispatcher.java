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
public abstract class CustomDispatcher<MsgType> implements Dispatcher<MsgType> {
    private static final Logger logger = LoggerFactory.getLogger(CustomDispatcher.class);

    public CustomDispatcher(Executor executor) {
        this.executor = executor;
    }

    public CustomDispatcher() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public CustomDispatcher(int poolSize) {
        this(poolSize <= 1 ? new SingleThreadExecutor() : new MultiThreadExecutor(poolSize));
    }

    private final Executor executor;

    public class CustomTask implements Task {
        CustomTask(Connection c, MsgType p) {
            this.c = c;
            this.p = p;
        }

        Connection c;
        MsgType p;

        @Override
        public void run() {
            try {
                customDispatch(c, p);
            } catch (Exception e) {
                logger.error("消息处理出错:\n", e);
            }
        }
    }

    public abstract void customDispatch(Connection c, MsgType m) throws Exception;

    @Override
    public void dispatch(Connection c, MsgType p) {
        executor.execute(new CustomTask(c, p));
    }
}
