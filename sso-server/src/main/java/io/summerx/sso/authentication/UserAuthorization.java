package io.summerx.sso.authentication;

import io.summerx.framework.sso.user.SSOUser;
import io.summerx.sso.authentication.generator.TicketGenerator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * 已经认证的用户身份信息
 *
 * @author summerx
 * @Date 2016-06-16
 */
public class UserAuthorization implements SSOUser, java.io.Serializable {

    // Tgt
    private final String ticket;

    // 用户名
    private final String username;

    // 客户端信息
    private final Object clientInfo;

    // 已经登录的应用信息
    private Set<GrantedApplication> apps;

    public UserAuthorization(String username, TicketGenerator generator) {
        this(username, generator, null);
    }

    public UserAuthorization(String username, TicketGenerator generator, Object clientInfo) {
        this.username = username;
        this.clientInfo = clientInfo;
        this.ticket = generator.generateTgt(this);
    }

    public String getUsername() {
        return username;
    }

    public Object getClientInfo() {
        return clientInfo;
    }

    public Collection<GrantedApplication> getApplications() {
        return apps;
    }

    public void addApplication(GrantedApplication app) {
        if (this.apps == null) {
            this.apps = new HashSet<>();
        }
        this.apps.add(app);
    }

    public GrantedApplication getApplication(String appname) {
        if (appname == null || this.apps == null) {
            return null;
        }
        for (GrantedApplication granted : apps) {
            if (appname.equals(granted.getName())) {
                return granted;
            }
        }
        return null;
    }
    public void removeApplication(String appname) {
        GrantedApplication app = getApplication(appname);
        if (app != null) {
            apps.remove(app);
        }
    }

    public String getTicket() {
        return ticket;
    }

}
