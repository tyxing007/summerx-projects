package io.summerx.sso.web.filter;

import io.summerx.framework.utils.StringUtils;
import io.summerx.framework.web.filter.AbstractUrlFilter;
import io.summerx.framework.web.utils.CookieUtils;
import io.summerx.framework.web.utils.WebClientInfo;
import io.summerx.sso.auth.AuthorizationManager;
import io.summerx.sso.auth.TicketCredentials;
import io.summerx.sso.auth.UsernamePasswordAuthorization;
import io.summerx.sso.auth.exception.AuthenticationException;
import io.summerx.sso.commons.SSOConstants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Server的SSO过滤器
 * @author summerx
 * @Date 2016-07-14 12:49 PM
 */
public class SSOFilter extends AbstractUrlFilter {

    private AuthorizationManager authManager;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 从Cookie中获取Tgt
        String tgt = CookieUtils.getCookieValue(request, SSOConstants.COOKIE_TGT);

        // 没有Tgt
        if (StringUtils.isEmpty(tgt)) {
            notLogin(request, response, filterChain);
            return;
        }

        try {
            // 验证Tgt
            UsernamePasswordAuthorization authorization = authManager.authenticate(new TicketCredentials(tgt, new WebClientInfo(request)));
            if (authorization == null) {
                notLogin(request, response, filterChain);
                return;
            }
        } catch (AuthenticationException ex) {
            notLogin(request, response, filterChain);
            return;
        }

        // 能走到这里说明已经登录
        try {
            filterChain.doFilter(request, response);
        } finally {

        }
    }

    // 没有登录
    protected void notLogin(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        response.sendRedirect("/sso/sso-login");
    }
}
