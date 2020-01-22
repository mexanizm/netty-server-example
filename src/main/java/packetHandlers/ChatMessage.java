package packetHandlers;

import java.io.Serializable;

public class ChatMessage extends Packet implements Serializable {

    private String message;

    public ChatMessage(){}

    public ChatMessage(String message) {
        super();
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
