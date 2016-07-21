package io.summerx.sso.authentication;

import io.summerx.sso.constants.SSOServerContants;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * Created by xiayg on 7/10/2016.
 */
public class GrantedApplication implements java.io.Serializable {

    // 应用名
    private final String name;

    // PT凭证
    private String tikcet;

    // Callback地址
    private final String url;

    // 创建时间
    private final Timestamp createTime;

    public GrantedApplication(String name) {
        this(name, null);
    }

    public GrantedApplication(String name, String url) {
        this.name = name;
        this.url = url;
        createTime = new Timestamp(System.currentTimeMillis());
    }

    public GrantedApplication(HttpServletRequest request) {
        this(request.getParameter(SSOServerContants.REQ_PARAM_APPNAME), request.getParameter(SSOServerContants.REQ_PARAM_HOOKURL));
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public String getUrl() {
        return url;
    }
}
