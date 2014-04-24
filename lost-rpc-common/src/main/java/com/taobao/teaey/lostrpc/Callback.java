package com.taobao.teaey.lostrpc;

import com.google.protobuf.RpcCallback;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaofei.wxf on 14-2-13.
 */
public interface Callback<AnswerType> extends RpcCallback<AnswerType>, Future<AnswerType> {
    public static final int ST_INIT = 1;
    public static final int ST_DONE = 2;
    public static final int ST_CANCELLED = 3;
    public static final int ST_TIMEOUT = 4;

    /**
     * called when get answer
     *
     * @param parameter
     */
    void run(AnswerType parameter);

    /**
     * called in run
     *
     * @param parameter
     */
    void process(AnswerType parameter);

    boolean await(long timeout, TimeUnit unit) throws InterruptedException;

    boolean await(long timeoutMillis) throws InterruptedException;

    boolean await() throws InterruptedException;

    boolean isTimeout();

    void done();

    void cancel();

    void timeout();

}
