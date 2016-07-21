package io.summerx.sso.authentication;

/**
 * 临时凭证（ST凭证）
 *
 * 用户第一次访问某个应用时，需要向SSO服务器获取一个临时凭证（ST凭证），然后将临时凭证提交给应用。
 * 应用会向SSO服务器验证此临时凭证，若合法则会颁发一个代理凭证（PT凭证），后续用户可以通过此代理凭证访问应用。
 *
 * @author summerx
 * @Date 2016-07-08
 */
public class ProvisionalCredentials extends TicketCredentials {

    // 应用
    private final String app;

    // 回调地址
    private String url;

    public ProvisionalCredentials(String ticket, String app) {
        this(ticket, app, null);
    }

    public ProvisionalCredentials(String ticket, String app, Object clientInfo) {
        super(ticket, clientInfo);
        this.app = app;
    }

    public String getApp() {
        return app;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
