package packetHandlers;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;
import java.util.UUID;

public class UserHandler implements Serializable {
    private ChannelHandlerContext cht;
    private UUID id;
    private Integer rl;
    private String login;

    public UserHandler(ChannelHandlerContext cht) {
        this.cht = cht;
    }

    public UserHandler(ChannelHandlerContext cht, UUID id, Integer rl, String login) {
        this.cht = cht;
        this.id = id;
        this.rl = rl;
        this.login = login;
    }

    public ChannelHandlerContext getCht() {
        return cht;
    }

    public void setCht(ChannelHandlerContext cht) {
        this.cht = cht;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getRl() {
        return rl;
    }

    public void setRl(Integer rl) {
        this.rl = rl;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
