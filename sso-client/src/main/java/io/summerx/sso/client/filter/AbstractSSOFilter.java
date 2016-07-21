package io.summerx.sso.client.filter;

import io.summerx.framework.sso.user.SSOUserHolder;
import io.summerx.framework.utils.RandomUtils;
import io.summerx.framework.utils.StringUtils;
import io.summerx.framework.web.filter.AbstractUrlFilter;
import io.summerx.framework.web.utils.WebUtils;
import io.summerx.sso.client.config.SSOClientConfig;
import io.summerx.sso.client.user.ClientSSOUser;
import io.summerx.sso.client.user.ClientSSOUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * SSO过滤器
 *
 * @author summerx
 * @Date 2016-07-12
 */
public abstract class AbstractSSOFilter extends AbstractUrlFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * SSO客户端配置
     */
    private SSOClientConfig ssoClientConfig;

    /**
     * 向SSO服务器验证ST并获取用户的服务
     */
    private ClientSSOUserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 解决跨域访问cookies的问题
        response.setHeader("P3P", "CP=CAO PSA OUR");

        // 获取当前登录用户
        ClientSSOUser userDetail = getCurrentUserObject(request, response);
        if (userDetail == null) {
            // 没有登录，则从请求参数中拿到ST
            String st = request.getParameter("st");
            if (StringUtils.isEmpty(st)) {
                // 没有ST，重定向到[sso-verify?app=&returnUrl=]去要一个ST
                redirect(ssoClientConfig.getVerifyUrl(), request, response);
                return;
            } else {
                // FIXME 有ST，验证ST合法性（访问[sso-validate?app=&st=]获取PT和登录用户信息）
                userDetail = userService.getAuthorizationUser(st);

                if (userDetail == null) {
                    logger.warn(String.format("Client[%s] Attempt to access system use an invalidation token[st = %s]",
                            WebUtils.getIpAddress(request), st));

                    // ST无效，重定向到[sso-login?returnUrl=]去登录
                    redirect(ssoClientConfig.getLoginUrl(), request, response);
                    return;
                } else {
                    // ST有效，保存用户信息
                    storeCurrentUserObject(request, response, userDetail, ssoClientConfig.getPtExpires());

                    // 记录日志
                    logger.info(String.format("用户[%s]认证成功，有效期%s秒", userDetail.getUsername(), ssoClientConfig.getPtExpires()));
                }
            }
        }

        try {
            // 设置用户信息（Thread级别）
            SSOUserHolder.set(userDetail);
            // 继续执行FliterChain
            filterChain.doFilter(request, response);
            return;
        } finally {
            // 清除用户信息
            SSOUserHolder.remove();
        }
    }

    /**
     * 获取当前登录用户
     */
    protected abstract ClientSSOUser getCurrentUserObject(HttpServletRequest request, HttpServletResponse response);

    /**
     * 保存当前登录用户
     *
     * @param userDetail 登录用户信息
     * @param expires    过期时间
     */
    protected abstract void storeCurrentUserObject(HttpServletRequest request, HttpServletResponse response, ClientSSOUser userDetail, int expires);

    protected void redirect(String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> extraParams = new HashMap<>();
        extraParams.put("app", "app");
        extraParams.put("rnd", RandomUtils.generateAlphaNumberSequence(8));
        // 如果有ST要去除st参数，防止循环想SSO服务器验证ST
        String urlToUse = encodeRetUrl(url, ssoClientConfig.getRetUrlParam(), WebUtils.getRequestURLWithQueryString(request, "st"), extraParams);
        response.sendRedirect(urlToUse);
    }

    protected String encodeRetUrl(String url, String retParam, String retUrl, Map<String, String> params) {
        StringBuffer sBuf = new StringBuffer(url);
        sBuf.append("?");
        sBuf.append(retParam);
        sBuf.append("=");
        try {
            sBuf.append(URLEncoder.encode(retUrl, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sBuf.append("&");
            sBuf.append(entry.getKey());
            sBuf.append("=");
            sBuf.append(entry.getValue());
        }

        return sBuf.toString();
    }

    public void setUserService(ClientSSOUserService userService) {
        this.userService = userService;
    }

    public void setSsoClientConfig(SSOClientConfig ssoClientConfig) {
        this.ssoClientConfig = ssoClientConfig;
    }
}
