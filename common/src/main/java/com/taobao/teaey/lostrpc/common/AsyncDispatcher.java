package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.concurrent.MultiThreadExecutor;
import com.taobao.teaey.lostrpc.concurrent.SingleThreadExecutor;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author xiaofei.wxf
 */
public abstract class AsyncDispatcher<MsgType> implements Dispatcher<Channel, MsgType> {
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

    class TaskImpl implements Task {
        TaskImpl(Channel channel, MsgType p) {
            this.channel = channel;
            this.p = p;
        }

        Channel channel;
        MsgType p;

        @Override
        public void run() {
            try {
                onDispatch(channel, p);
            } catch (Exception e) {
                logger.error("消息处理出错:\n", e);
            }
        }
    }

    public abstract void onDispatch(Channel channel, MsgType m) throws Exception;

    @Override
    public void dispatch(Channel channel, MsgType p) {
        executor.execute(new TaskImpl(channel, p));
    }
}
