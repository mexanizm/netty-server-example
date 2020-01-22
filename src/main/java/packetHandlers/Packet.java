package packetHandlers;

import java.io.Serializable;

public abstract class Packet implements Serializable {

    private UserHandler userHandler;

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public void setUserHandler(UserHandler userHandler) {
        this.userHandler = userHandler;
    }
}
