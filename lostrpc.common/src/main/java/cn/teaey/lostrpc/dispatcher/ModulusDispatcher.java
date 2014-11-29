package cn.teaey.lostrpc.dispatcher;

import cn.teaey.lostrpc.concurrent.ModulusExecutor;

/**
 * @author xiaofei.wxf
 */
public abstract class ModulusDispatcher<MsgType> extends CustomDispatcher<MsgType> {
    public ModulusDispatcher(int mod) {
        super(ModulusExecutor.newOne(mod));
    }

}
