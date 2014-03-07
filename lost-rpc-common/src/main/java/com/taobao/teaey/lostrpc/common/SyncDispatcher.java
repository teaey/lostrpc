package com.taobao.teaey.lostrpc.common;

import com.taobao.teaey.lostrpc.concurrent.SyncExecutor;

/**
 * Created by XiaoFei on 14-3-6.
 */
public abstract class SyncDispatcher<MsgType> extends CustomDispatcher<MsgType> {

    public SyncDispatcher() {
        super(new SyncExecutor());
    }
}
