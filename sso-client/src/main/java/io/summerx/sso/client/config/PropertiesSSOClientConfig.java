package io.summerx.sso.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * SSO客户端配置信息
 *
 * @author summerx
 * @Date 2016-07-12
 */
@Component("propertiesSSOClientConfig")
public class PropertiesSSOClientConfig implements SSOClientConfig {

    /**
     * Login页面地址
     */
    @Value("${sso.url.login:/sso/sso-login}")
    private String loginUrl;

    /**
     * Verify地址，用户访问应用前需要先向SSO服务器核实自己已经登录
     */
    @Value("${sso.url.verify:/sso/sso-verify}")
    private String verifyUrl;

    /**
     * 验证用户身份信息的地址，SSO服务器核实用户身份后会向用户颁发一个ST，用户向应用出示ST，应用需要通过ST向SSO服务器确认用户身份信息
     */
    @Value("${sso.url.user:/sso/sso-user.json}")
    private String userUrl;

    /**
     * 在验证用户身份的过程中，用户原先请求的Url需要保存在这个请求参数中，以便验证用户身份通过后跳转回用户原来申请的页面
     */
    @Value("${sso.paramname.return:retUrl}")
    private String retUrlParam;

    /**
     * 客户端应用回调地址，验证用户身份通过后保存在SSO服务器端，以便SSO服务器在合适的时候回调此地址通知应用
     */
    @Value("${sso.url.callback}")
    private String callbackUrl;

    /**
     * PT过期时间
     */
    @Value("${sso.timeout.pt:1800}")
    private int ptExpires;

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public String getRetUrlParam() {
        return retUrlParam;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public int getPtExpires() {
        return ptExpires;
    }
}
