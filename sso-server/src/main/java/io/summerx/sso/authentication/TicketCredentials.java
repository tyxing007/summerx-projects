package io.summerx.sso.authentication;

/**
 * Ticket凭证
 *  1. TGT凭证：用户登录成功后，会产生一个TGT凭证，后续用户可以通过TGT凭证访问SSO服务器
 *  2. 临时凭证（ST凭证）：用户第一次访问某个应用时，需要先向SSO服务器获取一个临时凭证（ST凭证）并出示给应用
 *  3. 代理凭证（PT凭证）：应用会向SSO服务器验证临时凭证，若合法则会颁发一个代理凭证（PT凭证），后续用户可以通过此代理凭证访问应用。
 *
 * @author summerx
 * @Date 2016-07-08
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
