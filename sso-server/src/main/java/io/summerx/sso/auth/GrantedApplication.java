package io.summerx.sso.auth;

import io.summerx.sso.commons.SSOConstants;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * Created by xiayg on 7/10/2016.
 */
public class GrantedApplication implements java.io.Serializable {

    // 应用名
    private final String name;

    // 授权票据
    private String tikcet;

    // Callback地址
    private final String callback;

    // 创建时间
    private final Timestamp createTime;

    public GrantedApplication(String name) {
        this(name, null);
    }

    public GrantedApplication(String name, String callback) {
        this.name = name;
        this.callback = callback;
        createTime = new Timestamp(System.currentTimeMillis());
    }

    public GrantedApplication(HttpServletRequest request) {
        this(request.getParameter(SSOConstants.REQ_PARAM_APPNAME), request.getParameter(SSOConstants.REQ_PARAM_HOOKURL));
    }

    public String getName() {
        return name;
    }

    public String getTikcet() {
        return tikcet;
    }

    public void setTikcet(String tikcet) {
        this.tikcet = tikcet;
    }

    public String getCallback() {
        return callback;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }
}
