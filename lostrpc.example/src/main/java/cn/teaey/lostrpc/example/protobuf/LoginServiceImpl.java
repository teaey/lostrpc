package cn.teaey.lostrpc.example.protobuf;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

/**
 * @author xiaofei.wxf
 */
public class LoginServiceImpl implements TestProto.LoginService.BlockingInterface {
    @Override
    public TestProto.Login_S2C login(RpcController controller, TestProto.Login_C2S request)
        throws ServiceException {
        return TestProto.Login_S2C.newBuilder().setTimestamp(System.currentTimeMillis()).build();
    }
}
