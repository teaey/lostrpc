package cn.teaey.lostrpc.concurrent;

/**
 * @author xiaofei.wxf
 */
public class SyncExecutor extends CustomExecutor {
    @Override
    public void exec(Runnable r) {
        r.run();
    }
}
