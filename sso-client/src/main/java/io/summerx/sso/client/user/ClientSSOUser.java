package io.summerx.sso.client.user;

import io.summerx.framework.sso.user.SimpleSSOUser;
import io.summerx.framework.sso.user.SSOUser;

/**
 * <p>
 * 请在这里添加类说明.
 * </p>
 *
 * @author summerx
 * @Date 2016-07-19 4:32 PM
 */
public class ClientSSOUser extends SimpleSSOUser implements SSOUser {

    // 登录凭证（PT）
    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
