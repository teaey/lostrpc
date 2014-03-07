package com.taobao.teaey.lostrpc.concurrent;

import com.taobao.teaey.lostrpc.common.CustomDispatcher;
import com.taobao.teaey.lostrpc.cuncurrent.CustomExecutor;

/**
 * @author xiaofei.wxf
 */
public class SyncExecutor extends CustomExecutor<CustomDispatcher.CustomTask> {
    @Override
    public void exec(CustomDispatcher.CustomTask customTask) {
        customTask.run();
    }
}