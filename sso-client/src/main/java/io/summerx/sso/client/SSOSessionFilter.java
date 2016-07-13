package io.summerx.sso.client;

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

    protected Object getCurrentUserObject(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return session.getAttribute(SESSION_SSO_USER);
    }

    protected void storeCurrentUserObject(HttpServletRequest request, HttpServletResponse response, Object userObject, int expires) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.setAttribute(SESSION_SSO_USER, userObject);
        session.setMaxInactiveInterval(expires);
    }
}
