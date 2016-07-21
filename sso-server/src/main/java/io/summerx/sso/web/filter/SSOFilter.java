package io.summerx.sso.web.filter;

import io.summerx.framework.utils.StringUtils;
import io.summerx.framework.web.filter.AbstractUrlFilter;
import io.summerx.framework.web.utils.CookieUtils;
import io.summerx.framework.web.utils.WebClientInfo;
import io.summerx.sso.authentication.AuthorizationManager;
import io.summerx.sso.authentication.TgtCredentials;
import io.summerx.sso.authentication.UserAuthorization;
import io.summerx.sso.authentication.exception.AuthenticationException;
import io.summerx.sso.constants.SSOServerContants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Server的SSO过滤器
 *
 * @author summerx
 * @Date 2016-07-14 12:49 PM
 */
public class SSOFilter extends AbstractUrlFilter {

    private AuthorizationManager authManager;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 从Cookie中获取Tgt
        String tgt = CookieUtils.getCookieValue(request, SSOServerContants.COOKIE_TGT);

        // 没有Tgt
        if (StringUtils.isEmpty(tgt)) {
            notLogin(request, response, filterChain);
            return;
        }

        try {
            // 验证Tgt
            UserAuthorization authorization = authManager.authenticate(new TgtCredentials(tgt, new WebClientInfo(request)));
            if (authorization == null) {
                notLogin(request, response, filterChain);
                return;
            }
        } catch (AuthenticationException ex) {
            notLogin(request, response, filterChain);
            return;
        }

        try {
            // 能走到这里说明已经登录
            filterChain.doFilter(request, response);
        } finally {

        }
    }

    // 没有登录
    protected void notLogin(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        response.sendRedirect("/sso/sso-login");
    }
}
