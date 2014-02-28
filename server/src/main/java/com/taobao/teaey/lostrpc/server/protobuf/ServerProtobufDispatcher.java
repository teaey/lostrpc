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
public class ServerProtobufDispatcher extends AsyncDispatcher<LostProto.Packet> {
    protected static final Logger logger = LoggerFactory.getLogger(ServerProtobufDispatcher.class);

    private static final Dispatcher dispatcher0 = new AsyncDispatcher<AsyncDispatcher>(){
        @Override
        public void onDispatch(Channel channel, AsyncDispatcher m) throws Exception {

        }
    };

    public ServerProtobufDispatcher() {
    }

    public ServerProtobufDispatcher(int poolSize) {
        super(poolSize);
    }

    public ServerProtobufDispatcher(Executor executor) {
        super(executor);
    }

    @Override
    public void onDispatch(Channel channel, LostProto.Packet p) throws Exception {
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
}
