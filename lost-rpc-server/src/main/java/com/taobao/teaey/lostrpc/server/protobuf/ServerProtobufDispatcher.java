package com.taobao.teaey.lostrpc.server.protobuf;

import com.google.protobuf.BlockingService;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.common.AsyncDispatcher;
import com.taobao.teaey.lostrpc.common.LostProto;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * @author xiaofei.wxf
 */
public class ServerProtobufDispatcher implements Dispatcher<Channel, LostProto.Packet> {
    protected static final Logger logger = LoggerFactory.getLogger(ServerProtobufDispatcher.class);

    private final Dispatcher dispatcher0;

    /**
     * 默认是同步分发
     */
    public ServerProtobufDispatcher() {
        this(false);
    }

    /**
     * @param isAsync true 异步分发  false 同步分发
     */
    public ServerProtobufDispatcher(boolean isAsync) {
        if (isAsync) {
            dispatcher0 = new AsyncDispatcher<Channel, LostProto.Packet>() {
                @Override
                public void asyncDispatch(Channel c, LostProto.Packet m) throws Exception {
                    dispatch0(c, m);
                }
            };
        } else {
            dispatcher0 = new Dispatcher<Channel, LostProto.Packet>() {
                @Override
                public void dispatch(Channel c, LostProto.Packet m) {
                    dispatch0(c, m);
                }
            };
        }
    }

    public ServerProtobufDispatcher(Executor exec) {
        if (null == exec) {
            throw new NullPointerException("exec");
        }
        dispatcher0 = new AsyncDispatcher<Channel, LostProto.Packet>(exec) {
            @Override
            public void asyncDispatch(Channel c, LostProto.Packet m) throws Exception {
                dispatch0(c, m);
            }
        };

    }

    public void dispatch0(Channel channel, LostProto.Packet p) {
        try {
            String methodName = p.getMethodName();
            String serviceName = p.getServiceName();
            BlockingService service = ProtobufRegisterCenter.getBlockingService(serviceName);
            if (null == service) {
                logger.warn("找不到服务:{}", serviceName);
                return;
            }
            Descriptors.MethodDescriptor md = service.getDescriptorForType().findMethodByName(methodName);
            if (null == md) {
                logger.warn("找不到方法:{}", serviceName);
                return;
            }

            handleProtobufMsg(channel, p, service, md);
        } catch (Exception e) {
            logger.error("请求分发时出错:", e);
        }
    }

    /**
     * 处理PB消息，如果有特殊需求，比如添加Session机制、添加调用上下文
     * <p/>
     * 可以复写此方法，通过Colltroller传递或者其他方法来实现
     *
     * @param channel
     * @param p
     * @param service
     * @param md
     * @throws Exception
     */
    public void handleProtobufMsg(Channel channel, LostProto.Packet p, BlockingService service, Descriptors.MethodDescriptor md) throws Exception {
        Message resp = service.callBlockingMethod(md, null, service.getRequestPrototype(md).getParserForType().parseFrom(p.getData()));
        channel.writeAndFlush(LostProto.Packet.newBuilder(p).setData(resp.toByteString()).build());
    }

    @Override
    public void dispatch(Channel c, LostProto.Packet p) {
        dispatcher0.dispatch(c, p);
    }
}
