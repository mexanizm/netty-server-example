package exampleServer.Workers;

import exampleServer.ChanelsRegister;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.concurrent.PromiseCombiner;
import packetHandlers.Packet;
import packetHandlers.UserHandler;

public class ClientMessageWorker extends MessageWorker{

    public ClientMessageWorker(){
        super();
    }

    public ClientMessageWorker acceptPacket(Packet packet , ChannelHandlerContext channelHandlerContext){
        PacketWorkers pw = PacketWorkers.getByPacket(packet);
        if(pw != null){
            try {
                pw.getWorker().newInstance()
                        .setIncomingPacket(packet)
                        .setChanelHandler(channelHandlerContext)
                        .handle();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this;
    }



    @Override
    protected ClientMessageWorker setChanelHandler(ChannelHandlerContext chanelHandler) {
        super.setChanelHandler(chanelHandler);
        return this;
    }

    @Override
    protected ChannelHandlerContext getChanelHandler() {
        return super.getChanelHandler();
    }

    @Override
    protected ClientMessageWorker setIncomingPacket(Packet packet) {
        super.setIncomingPacket(packet);
        return this;
    }

    @Override
    protected void handle() {

    }

    @Override
    protected void broadcast(UserHandler userHandler, Object message){
        ChannelGroup channels = ChanelsRegister.getAllChannels();
        PromiseCombiner promiseCombiner = new PromiseCombiner();
        channels.stream()
//                .filter(c -> c != userHandler.getCht().channel())
                .forEach(c -> {
                    promiseCombiner.add(c.writeAndFlush(message).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
//                                System.out.println("Failed to write to channel: {}" + future.cause());
                                future.cause().printStackTrace();
                            }
                        }
                    }));
                });
    }

    @Override
    protected void loopbackToUser(Object message) {
        this.getChanelHandler().channel().writeAndFlush(message).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(!channelFuture.isSuccess()){
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }

    @Override
    protected void sendToUser(Channel channel, Object message) {
        channel.writeAndFlush(message);
    }
}
