package com.taobao.teaey.lostrpc.dispatcher;

import com.taobao.teaey.lostrpc.Connection;
import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.concurrent.IdentityTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author xiaofei.wxf
 */
public abstract class CustomDispatcher<MsgType> implements Dispatcher<MsgType> {
    private static final Logger logger = LoggerFactory.getLogger(CustomDispatcher.class);

    public CustomDispatcher(Executor executor) {
        if (null == executor) {
            throw new NullPointerException("executor");
        }
        this.executor = executor;
    }

    private final Executor executor;


    public class CustomTask implements IdentityTask {
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


        Connection c;
        MsgType p;

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

    public abstract void customDispatch(Connection c, MsgType m) throws Exception;

    @Override
    public void dispatch(Connection c, MsgType p) {
        executor.execute(new CustomTask(c, p));
    }
}
