package io.summerx.sso.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * SSO客户端配置信息
 *
 * @author summerx
 * @Date 2016-07-12
 */
public interface SSOClientConfig {

    /**
     * Login页面地址
     */
    String getLoginUrl();

    /**
     * Verify地址，用户访问应用前需要先向SSO服务器核实自己已经登录
     */
    String getVerifyUrl();

    /**
     * 验证用户身份信息的地址，SSO服务器核实用户身份后会向用户颁发一个ST，用户向应用出示ST，应用需要通过ST向SSO服务器确认用户身份信息
     */
    String getUserUrl();

    /**
     * 在验证用户身份的过程中，用户原先请求的Url需要保存在这个请求参数中，以便验证用户身份通过后跳转回用户原来申请的页面
     */
    String getRetUrlParam();

    /**
     * 客户端应用回调地址，验证用户身份通过后保存在SSO服务器端，以便SSO服务器在合适的时候回调此地址通知应用
     */
    String getCallbackUrl();

    /**
     * PT过期时间
     */
    int getPtExpires();
}
