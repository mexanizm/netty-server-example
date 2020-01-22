package exampleServer.Workers;

import exampleServer.Utils.ObjectCloneUtils;
import packetHandlers.ChatMessage;

public class ChatMessageWorker extends ClientMessageWorker{

    public ChatMessageWorker(){
        super();
    }

    @Override
    public void handle() {
        ChatMessage packet = (ChatMessage) this.getIncomingPacket();
        this.loopbackToUser(new ObjectCloneUtils<ChatMessage>().dublicate(packet));
    }


}
