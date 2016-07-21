package io.summerx.sso.constants;

/**
 * <p>
 * 请在这里添加类说明.
 * </p>
 *
 * @author summerx
 * @Date 2016-07-20 9:52 AM
 */
public class SSOServerContants {

    /**
     * ST的请求参数
     */
    public static final String REQ_PARAM_ST = "st";

    /**
     * 返回地址的请求参数
     */
    public static final String REQ_PARAM_RETURL = "retUrl";

    /**
     * 应用名的请求参数
     */
    public static final String REQ_PARAM_APPNAME = "app";

    /**
     * 应用回调地址的请求参数
     */
    public static final String REQ_PARAM_HOOKURL = "url";

    /**
     * SSO服务器退出操作
     */
    public static final String ACTION_LOGOUT = "logout";

    /**
     * TGT凭证在客户端浏览器中的Cookie Name
     */
    public static final String COOKIE_TGT = "SSO_TGT";
}