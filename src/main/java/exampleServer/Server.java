package exampleServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import packetHandlers.Packet;

public class Server {

    private int port;
    //Set new thread to execute a logic pipline handler
    static final EventExecutorGroup executorGroup= new DefaultEventExecutorGroup(16);

    public Server(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            //final DiscardServerHandler handler = new DiscardServerHandler();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(8*1024*1024));
                            ch.pipeline().addLast("decoder" , new ObjectDecoder(ClassResolvers.cacheDisabled(Packet.class.getClassLoader())));
                            ch.pipeline().addLast("encoder" , new ObjectEncoder());
                            ch.pipeline().addLast( executorGroup , "handler" , new ServerHandler());
                        }

                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
