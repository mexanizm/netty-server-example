package exampleServer;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChanelsRegister {

    private static ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void addChannel(Channel channel){
        allChannels.add(channel);
    }

    public static ChannelGroup getAllChannels(){
        return allChannels;
    }

}
