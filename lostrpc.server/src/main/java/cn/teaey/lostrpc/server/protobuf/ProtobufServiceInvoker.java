package cn.teaey.lostrpc.server.protobuf;

import cn.teaey.lostrpc.RpcSession;
import cn.teaey.lostrpc.common.LostProto;
import cn.teaey.lostrpc.server.ServiceInvoker;
import com.google.protobuf.BlockingService;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;

/**
 * @author xiaofei.wxf
 */
public class ProtobufServiceInvoker implements ServiceInvoker {
    private ProtobufServiceInvoker() {
    }

    public static ProtobufServiceInvoker theOne() {
        return Holder.i;
    }

    @Override
    public Object handle(RpcSession session, Object packet) {
        try {
            LostProto.Packet p = (LostProto.Packet) packet;
            String methodName = p.getMethodName();
            String serviceName = p.getServiceName();
            BlockingService service =
                (BlockingService) ProtobufServiceCenter.theOne().get(serviceName);
            if (null == service) {
                throw new RuntimeException("找不到服务 " + serviceName);
            }
            Descriptors.MethodDescriptor md =
                service.getDescriptorForType().findMethodByName(methodName);
            if (null == md) {
                throw new RuntimeException("找不到方法 " + methodName);
            }

            MessageLite respMsg = (MessageLite) handleProtobufMsg(p, service, md);
            LostProto.Packet respPacket =
                LostProto.Packet.newBuilder(p).setData(respMsg.toByteString()).build();
            return respPacket;
        } catch (Exception e) {
            throw new RuntimeException("处理请求出错", e);
        }
    }

    /**
     * 处理PB消息，如果有特殊需求，比如添加Session机制、添加调用上下文
     * <p/>
     * 可以复写此方法，通过Colltroller传递或者其他方法来实现
     *
     * @param p
     * @param service
     * @param md
     * @throws Exception
     */
    public Object handleProtobufMsg(LostProto.Packet p, BlockingService service,
        Descriptors.MethodDescriptor md) throws Exception {
        Message resp = service.callBlockingMethod(md, null,
            service.getRequestPrototype(md).getParserForType().parseFrom(p.getData()));
        return resp;
    }


    private static class Holder {
        static final ProtobufServiceInvoker i = new ProtobufServiceInvoker();
    }
}
