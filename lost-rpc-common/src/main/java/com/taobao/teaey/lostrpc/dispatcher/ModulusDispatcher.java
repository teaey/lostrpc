package com.taobao.teaey.lostrpc.dispatcher;

import com.taobao.teaey.lostrpc.concurrent.ModulusExecutor;

/**
 * @author xiaofei.wxf
 */
public abstract class ModulusDispatcher<MsgType> extends CustomDispatcher<MsgType> {
    public ModulusDispatcher(int mod) {
        super(ModulusExecutor.newOne(mod));
    }

}
