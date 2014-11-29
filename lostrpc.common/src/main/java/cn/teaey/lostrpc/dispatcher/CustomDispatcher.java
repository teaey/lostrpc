package cn.teaey.lostrpc.dispatcher;

import cn.teaey.lostrpc.Connection;
import cn.teaey.lostrpc.Dispatcher;
import cn.teaey.lostrpc.concurrent.IdentityTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author xiaofei.wxf
 */
public abstract class CustomDispatcher<MsgType> implements Dispatcher<MsgType> {
    private static final Logger logger = LoggerFactory.getLogger(CustomDispatcher.class);
    private final Executor executor;

    public CustomDispatcher(Executor executor) {
        if (null == executor) {
            throw new NullPointerException("executor");
        }
        this.executor = executor;
    }

    public abstract void customDispatch(Connection c, MsgType m) throws Exception;

    @Override
    public void dispatch(Connection c, MsgType p) {
        executor.execute(new CustomTask(c, p));
    }


    public class CustomTask implements IdentityTask {
        Connection c;
        MsgType p;
        CustomTask(Connection c, MsgType p) {
            if (null == c) {
                throw new NullPointerException("connection");
            }
            if (null == p) {
                throw new NullPointerException("packet");
            }
            this.c = c;
            this.p = p;
        }

        @Override
        public void run() {
            try {
                customDispatch(c, p);
            } catch (Exception e) {
                logger.error("CustomDispatch packet error:\n", e);
            }
        }

        @Override public long id() {
            return c.getId();
        }
    }
}
