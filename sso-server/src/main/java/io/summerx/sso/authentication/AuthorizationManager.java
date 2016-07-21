package io.summerx.sso.authentication;

/**
 * SSO授权认证管理
 *  1. 验证登录凭证，成功则返回授权的用户身份信息
 *  2. 验证TGT凭证（登录成功后颁发给用户），成功则返回授权的用户身份信息
 *  3. 颁发临时凭证（ST凭证），用户第一次访问某个应用时，向SSO服务器索要一个临时凭证（ST凭证）。
 *  4. 验证临时凭证（用户向应用出示临时凭证后，应用会向SSO服务器验证此临时凭证，若合法则会颁发一个代理凭证（PT凭证），后续用户可以通过此代理凭证访问应用）
 *  5. 销毁TGT凭证
 *  6. 用户申请ST凭证时，判断是否允许访问指定应用资源
 * 生成st
 * 验证st
 * 生成pt
 *
 * @author summerx
 * @Date 2016-06-16
 */
public interface AuthorizationManager {

    /**
     * 验证登录凭证，成功则返回授权的用户身份信息
     * @param credentials 登录凭证（用户名密码等）
     * @return 授权的用户身份信息
     */
    UserAuthorization authenticate(UsernamePasswordCredentials credentials);

    /**
     * 验证TGT凭证，成功则返回授权的用户身份信息
     * @param credentials 用户TGT信息
     * @return
     */
    UserAuthorization authenticate(TgtCredentials credentials);

    /**
     * 颁发临时凭证（ST凭证）
     *
     * @param authorization
     * @return
     */
    ProvisionalCredentials issueProvisionalCredentials(UserAuthorization authorization, String app);

    /**
     * 应用向SSO服务器验证用户提交的ST，验证通过则返回PT凭证
     *
     * @param credentials ST凭证
     * @return
     */
    UserAuthorization validateSt(ProvisionalCredentials credentials);

    /**
     * 销毁TGT凭证，TGT凭证销毁后，需要通知应用销毁各自保存的PT凭证
     *
     * @param credentials TGT凭证
     */
     void destoryTgt(TgtCredentials credentials);

    /**
     * 用户申请ST凭证时，判断是否允许访问指定应用资源
     * @param authorization
     * @param appname
     * @return
     */
     boolean accessiable(UserAuthorization authorization, String appname);
}
