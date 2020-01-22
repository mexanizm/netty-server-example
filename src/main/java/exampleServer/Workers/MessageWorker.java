package exampleServer.Workers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import packetHandlers.Packet;
import packetHandlers.UserHandler;

public abstract class MessageWorker {

    private volatile Packet incomingPacket;

    private volatile ChannelHandlerContext chanelHandler;

    public MessageWorker(){
        super();
    }

    protected MessageWorker setIncomingPacket(Packet packet){
        this.incomingPacket = packet;
        return this;
    }

    protected Packet getIncomingPacket() {
        return incomingPacket;
    }

    protected ChannelHandlerContext getChanelHandler() {
        return chanelHandler;
    }

    protected MessageWorker setChanelHandler(ChannelHandlerContext ctx) {
        chanelHandler = ctx;
        return this;
    }

    protected abstract void handle();

    protected abstract void broadcast(UserHandler userHandler, Object message);

    protected abstract void loopbackToUser(Object message);

    protected abstract void sendToUser(Channel channel , Object message);
}
