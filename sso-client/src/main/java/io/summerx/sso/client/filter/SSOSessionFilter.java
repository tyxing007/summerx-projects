package io.summerx.sso.client.filter;

import io.summerx.sso.client.user.ClientSSOUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 基于Session的SSO过滤器
 *
 * @author summerx
 * @Date 2016-07-12
 */
public class SSOSessionFilter extends AbstractSSOFilter {

    public static final String SESSION_SSO_USER = "__sso.client.user";

    protected ClientSSOUser getCurrentUserObject(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (ClientSSOUser) session.getAttribute(SESSION_SSO_USER);
    }

    protected void storeCurrentUserObject(HttpServletRequest request, HttpServletResponse response, ClientSSOUser userDetail, int expires) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.setAttribute(SESSION_SSO_USER, userDetail);
        session.setMaxInactiveInterval(expires);
    }
}
