package cn.teaey.lostrpc.client;

import cn.teaey.lostrpc.Cmd;
import cn.teaey.lostrpc.Dispatcher;
import cn.teaey.lostrpc.LostRpcWrapException;
import cn.teaey.lostrpc.NettyChannelInitializer;
import cn.teaey.lostrpc.common.DispatchHandler;
import cn.teaey.lostrpc.common.Safety;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author xiaofei.wxf on 14-2-13.
 */
public class NettyClient<ReqType, RespType>
    implements Client<ReqType, RespType, Channel, NettyClient> {
    private final Bootstrap b = newBootstrap();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private SocketAddress addr;
    private NettyChannelInitializer initializer;
    private Channel channel;

    private DispatchHandler<RespType> dispatcherHandler;

    private Safety safety;

    private NettyClient() {
    }

    public static NettyClient newInstance() {
        return new NettyClient();
    }

    public NettyClient initializer(NettyChannelInitializer initializer) {
        this.initializer = initializer;
        this.safety = this.initializer.getSafety();
        if (null != this.dispatcherHandler) {
            this.initializer.bridgeHandler(this.dispatcherHandler);
        }
        return this;
    }

    public NettyClient dispatcher(Dispatcher dispatcher) {
        this.dispatcherHandler = new DispatchHandler(dispatcher);
        if (this.initializer != null) {
            this.initializer.bridgeHandler(dispatcherHandler);
        }
        return this;
    }

    @Override
    public NettyClient run() {
        if (null == addr) {
            throw new NullPointerException("not connect");
        }
        if (null == initializer) {
            throw new NullPointerException("channel initializer not set");
        }
        try {
            b.group(workerGroup).channel(NioSocketChannel.class).handler(this.initializer);
            ChannelFuture f = b.connect(addr).sync();
            this.channel = f.channel();
            return this;
        } catch (Throwable e) {
            shutdown();
            throw new LostRpcWrapException("Netty Client connect failed:", e);
        }

    }

    @Override
    public NettyClient shutdown() {
        workerGroup.shutdownGracefully();
        return this;
    }

    @Override
    public NettyClient connect(InetSocketAddress address) {
        this.addr = address;
        return this;
    }

    @Override
    public NettyClient showdownNow() {
        return shutdown();
    }

    @Override
    public ChannelFuture ask(ReqType p) {
        return this.channel.writeAndFlush(p);
    }

    public NettyClient handshake() {
        ByteBuf buf = Unpooled.buffer(5);
        buf.writeInt(0);
        buf.writeByte(Cmd.CMD_SYNCKEY_REQ.getType());
        this.channel.writeAndFlush(buf);
        this.safety.syncAuthed();
        return this;
    }

    protected Bootstrap newBootstrap() {
        return new Bootstrap().option(ChannelOption.SO_LINGER, -1)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
            .option(ChannelOption.TCP_NODELAY, true);
    }
}
