package com.taobao.teaey.lostrpc;

/**
 * @author xiaofei.wxf
 */
public enum Cmd {
    CMD_SYNCKEY_REQ(1, "更新公钥请求"),
    CMD_SYNCKEY_RES(2, "更新公钥响应"),
    CMD_HANDSHAKE_REQ(3, "握手请求"),
    CMD_HANDSHAKE_RES(4, "握手响应"),
    CMD_DATA(5, "数据请求");

    public static Cmd match(int type) {
        for (Cmd each : Cmd.values()) {
            if (type == each.getType()) {
                return each;
            }
        }
        throw new UndefinedCmdException(type);
    }

    public static class UndefinedCmdException extends RuntimeException {
        UndefinedCmdException(int type) {
            super("Cmd type " + type);
        }
    }

    private byte type;
    private String desc;

    Cmd(int type, String desc) {
        this.type = (byte) type;
        this.desc = desc;
    }

    public byte getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Cmd{" +
                "type=" + type +
                ", desc='" + desc + '\'' +
                '}';
    }
}
