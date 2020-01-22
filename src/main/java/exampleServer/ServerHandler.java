package exampleServer;

import exampleServer.Workers.ClientMessageWorker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import packetHandlers.Packet;
import packetHandlers.UserHandler;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    ServerHandler(){
        super();
        System.out.println("Init::ServerHandler");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object frame) throws Exception {
        Packet pc = (Packet) frame;
        if(pc.getUserHandler() == null){
            pc.setUserHandler(new UserHandler(ctx));
        }
        new ClientMessageWorker().acceptPacket(pc , ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("added chennel " + ctx.channel());
        ChanelsRegister.addChannel(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext arg0, Throwable arg1) throws Exception {
        arg1.printStackTrace();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
        System.out.println("handlerAdded >> " + arg0);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
        System.out.println("handlerRemoved >> " + arg0);
    }

}
