package io.summerx.sso.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiayg on 7/8/2016.
 */
public class UsernamePasswordAuthorization implements java.io.Serializable {

    // Token granted ticket
    private String tgt;

    // 用户名
    private final String username;

    // 客户端信息
    private final Object clientInfo;

    // 获得的授权信息
    private final Collection<Object> authorities;

    // 已经登录的应用信息
    private Set<GrantedApplication> apps;

    public UsernamePasswordAuthorization(String username) {
        this(username, null);
    }

    public UsernamePasswordAuthorization(String username, Object clientInfo) {
        this(username, clientInfo, null);
    }

    public UsernamePasswordAuthorization(String username, Object clientInfo, Collection<Object> authorities) {
        this.username = username;
        this.clientInfo = clientInfo;
        this.authorities = authorities;
    }

    public String getTgt() {
        return tgt;
    }

    public void setTgt(String tgt) {
        this.tgt = tgt;
    }

    public String getUsername() {
        return username;
    }

    public Object getClientInfo() {
        return clientInfo;
    }

    public Collection<Object> getAuthorities() {
        return authorities;
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
}
