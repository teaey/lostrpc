package cn.teaey.lostrpc.dispatcher;


import cn.teaey.lostrpc.concurrent.AsyncExecutor;

/**
 * Created by XiaoFei on 14-3-6.
 */
public abstract class AsyncDispatcher<MsgType> extends CustomDispatcher<MsgType> {
    public AsyncDispatcher(int threadNum) {
        super(AsyncExecutor.newOne(threadNum));
    }

    public AsyncDispatcher() {
        this(Runtime.getRuntime().availableProcessors());
    }
}
