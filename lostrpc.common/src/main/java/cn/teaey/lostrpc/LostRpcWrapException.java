package cn.teaey.lostrpc;

/**
 * Created by xiaofei.wxf on 2014/11/29.
 */
public class LostRpcWrapException extends RuntimeException {
    public LostRpcWrapException() {
    }

    public LostRpcWrapException(String message) {
        super(message);
    }

    public LostRpcWrapException(String message, Throwable cause) {
        super(message, cause);
    }

    public LostRpcWrapException(Throwable cause) {
        super(cause);
    }

    public LostRpcWrapException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
