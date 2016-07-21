package io.summerx.sso.authentication;

/**
 * TGT凭证
 *
 * @author summerx
 * @Date 2016-07-08
 */
public class TgtCredentials extends TicketCredentials {

    public TgtCredentials(String ticket) {
        this(ticket, null);
    }

    public TgtCredentials(String ticket, Object clientInfo) {
        super(ticket, clientInfo);
    }
}
