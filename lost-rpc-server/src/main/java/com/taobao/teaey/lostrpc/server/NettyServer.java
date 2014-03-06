package com.taobao.teaey.lostrpc.server;


import com.taobao.teaey.lostrpc.Dispatcher;
import com.taobao.teaey.lostrpc.NettyChannelInitializer;
import com.taobao.teaey.lostrpc.common.DispatchHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by xiaofei.wxf on 14-2-13.
 */
public class NettyServer<ReqType> implements Server<Channel, ReqType, NettyServer> {
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final ServerBootstrap b = newServerBootStrap();
    private SocketAddress addr;

    private NettyChannelInitializer initializer;

    private Dispatcher<ReqType> dispatcher;

    public static NettyServer newInstance() {
        return new NettyServer();
    }

    private NettyServer() {
    }

    public NettyServer initializer(NettyChannelInitializer initializer) {
        this.initializer = initializer;
        if (this.dispatcher != null)
            this.initializer.dispatchHandler(new DispatchHandler(this.dispatcher));
        return this;
    }

    @Override
    public NettyServer run() {
        if (null == addr) {
            throw new NullPointerException("socket addr");
        }
        if (null == initializer) {
            throw new NullPointerException("channel initializer");
        }
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(this.initializer);
        try {
            ChannelFuture f = b.bind(addr).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            shutdown();
        }
        return this;
    }

    @Override
    public NettyServer shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        return this;
    }

    @Override
    public NettyServer bind(int port) {
        this.addr = new InetSocketAddress(port);
        return this;
    }

    @Override
    public NettyServer bind(SocketAddress address) {
        this.addr = address;
        return this;
    }

    @Override
    public NettyServer showdownNow() {
        shutdown();
        return this;
    }

    @Override
    public NettyServer dispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        if (this.initializer != null) {
            this.initializer.dispatchHandler(new DispatchHandler(this.dispatcher));
        }
        return this;
    }

    protected ServerBootstrap newServerBootStrap() {
        return new ServerBootstrap().option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 100000)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_LINGER, -1)
                .childOption(ChannelOption.TCP_NODELAY, true);
    }
}
