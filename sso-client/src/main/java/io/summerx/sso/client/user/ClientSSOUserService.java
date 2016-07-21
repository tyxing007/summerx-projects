package io.summerx.sso.client.user;

/**
 * 客户端（业务应用）通过ST和应用名称向SSO服务器验证ST有效并获取用户授权的信息
 *
 * @author summerx
 * @Date 2016-07-14 3:02 PM
 */
public interface ClientSSOUserService {

    /**
     * 客户端（业务应用）通过ST和应用名称向SSO服务器验证ST有效并获取用户授权的信息
     *
     * @param st
     * @return
     */
    ClientSSOUser getAuthorizationUser(String st);
}
