package cn.teaey.lostrpc;

import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaofei.wxf on 14-2-13.
 */
public abstract class LostRpcCallback<AnswerType> implements Callback<AnswerType> {
    private final Channel channel;
    private final long created = System.currentTimeMillis();
    private final long timeout;//in millis
    private volatile int state = ST_INIT;
    private AnswerType result;

    public LostRpcCallback(Channel channel) {
        this.channel = channel;
        this.timeout = 0;
    }

    public LostRpcCallback(Channel channel, long timeout, TimeUnit unit) {
        if (null == channel) {
            throw new NullPointerException("channel");
        }
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        this.channel = channel;
        timeout = Math.max(timeout, 0);
        this.timeout = timeout;
    }

    @Override
    public void run(AnswerType parameter) {
        if (isCancelled()) {
            return;
        }
        if (timeout > 0 && System.currentTimeMillis() > (created + timeout)) {
            timeout();
            return;
        }
        this.result = parameter;

        process(parameter);

        done();
    }

    @Override
    public abstract void process(AnswerType parameter);

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return await(unit.toMillis(timeout));
    }

    @Override
    public boolean await(long timeoutMillis) throws InterruptedException {
        if (state == ST_INIT) {
            synchronized (this) {
                long until = System.currentTimeMillis() + timeoutMillis;
                while (state == ST_INIT) {
                    long sleep = until - System.currentTimeMillis();
                    if (sleep > 0) {
                        wait(sleep);
                    } else {
                        return isDone();
                    }
                }
            }
        }
        return isDone();
    }

    @Override
    public boolean await() throws InterruptedException {
        if (state == ST_INIT) {
            synchronized (this) {
                while (state == ST_INIT) {
                    wait();
                }
            }
        }
        return isDone();
    }

    @Override
    public boolean isDone() {
        return state == ST_DONE;
    }

    @Override
    public boolean isTimeout() {
        if (timeout == 0) {
            return false;
        }
        return state == ST_TIMEOUT;
    }

    @Override
    public boolean isCancelled() {
        return state == ST_CANCELLED;
    }


    @Override
    public void done() {
        state = ST_DONE;
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void cancel() {
        state = ST_CANCELLED;
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void timeout() {
        state = ST_TIMEOUT;
        synchronized (this) {
            notifyAll();
        }
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        cancel();
        return true;
    }

    @Override
    public AnswerType get() throws InterruptedException {
        await();
        return result;
    }

    @Override
    public AnswerType get(long timeout, TimeUnit unit) throws InterruptedException {
        await(timeout, unit);
        return result;
    }
}
