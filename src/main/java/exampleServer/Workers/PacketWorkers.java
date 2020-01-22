package exampleServer.Workers;

import packetHandlers.Packet;

public enum PacketWorkers {
    CHAT("ChatMessage" , ChatMessageWorker.class);

    private final String packetName;

    private final Class<? extends MessageWorker> worker;

    PacketWorkers(String packetName, Class<? extends MessageWorker> worker) {
        this.packetName = packetName;
        this.worker = worker;
    }

    public static PacketWorkers getByPacket(Packet packet){
        for (PacketWorkers value : PacketWorkers.values()) {
            if(value.packetName.equals(packet.getClass().getSimpleName())){
                return value;
            }
        }
        return null;
    }

    public Class<? extends MessageWorker> getWorker() {
        return worker;
    }

}
