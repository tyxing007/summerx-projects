package io.summerx.sso.auth;

/**
 * Created by xiayg on 7/8/2016.
 */
public class TicketCredentials implements java.io.Serializable {

    // 用户名
    private String ticket;

    // 客户端信息
    private Object clientInfo;

    public TicketCredentials(String ticket) {
        this(ticket, null);
    }

    public TicketCredentials(String ticket, Object clientInfo) {
        this.ticket = ticket;
        this.clientInfo = clientInfo;
    }

    public String getTicket() {
        return ticket;
    }

    public Object getClientInfo() {
        return clientInfo;
    }
}
