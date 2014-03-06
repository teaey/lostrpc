package com.taobao.teaey.lostrpc.common;

/**
 * Created by XiaoFei on 14-3-6.
 */
public abstract class SyncDispatcher<MsgType> extends CustomDispatcher<MsgType> {

    public SyncDispatcher() {
        super(new CustomExecutor.SyncExecutor());
    }
}
