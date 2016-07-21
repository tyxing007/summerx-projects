package io.summerx.sso.authentication;

/**
 * Created by xiayg on 7/8/2016.
 */
public class UsernamePasswordCredentials implements java.io.Serializable {

    // 用户名
    private final String username;

    // 密码
    private final String password;

    // 验证码
    private final String captcha;

    // 客户端信息
    private final Object clientInfo;

    public UsernamePasswordCredentials(String username, String password) {
        this(username, password, null);
    }

    public UsernamePasswordCredentials(String username, String password, String captcha) {
        this(username, password, captcha, null);
    }

    public UsernamePasswordCredentials(String username, String password, String captcha, Object clientInfo) {
        this.username = username;
        this.password = password;
        this.captcha = captcha;
        this.clientInfo = clientInfo;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public Object getClientInfo() {
        return clientInfo;
    }
}
