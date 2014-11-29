package cn.teaey.lostrpc;

/**
 * Created by xiaofei.wxf on 2014/11/29.
 */
public class LostRpcException extends RuntimeException {
    public LostRpcException() {
    }

    public LostRpcException(String message) {
        super(message);
    }

    public LostRpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public LostRpcException(Throwable cause) {
        super(cause);
    }

    public LostRpcException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
