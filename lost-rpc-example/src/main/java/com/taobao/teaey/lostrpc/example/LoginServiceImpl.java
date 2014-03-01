package com.taobao.teaey.lostrpc.example;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import com.taobao.teaey.lostrpc.TestProto;

/**
 * @author xiaofei.wxf
 */
public class LoginServiceImpl implements TestProto.LoginService.BlockingInterface {
    @Override
    public TestProto.Login_S2C login(RpcController controller, TestProto.Login_C2S request) throws ServiceException {
        return TestProto.Login_S2C.newBuilder().setTimestamp(System.currentTimeMillis()).build();
    }
}
