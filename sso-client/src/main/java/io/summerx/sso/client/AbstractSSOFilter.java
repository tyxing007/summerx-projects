package io.summerx.sso.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.summerx.framework.rpc.rest.spring.interceptor.LoggerClientHttpRequestInterceptor;
import io.summerx.framework.utils.RandomUtils;
import io.summerx.framework.utils.StringUtils;
import io.summerx.framework.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SSO过滤器
 *
 * @author summerx
 * @Date 2016-07-12
 */
public abstract class AbstractSSOFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // PT过期时间
    public static final int PT_EXP = 1800;

    static final String PARAM_RETURL = "retUrl";
    static final String SSO_DOMAIN = "/";
    static final String LOGIN_URL = SSO_DOMAIN + "/sso/sso-login";
    static final String VERIFY_URL = SSO_DOMAIN + "/sso/sso-verify";
    static final String USER_URL = "http://127.0.0.1:8080" + "/sso/sso-user.json";  // 注意内网访问地址

    protected UserObject getAuthorizationUser(String st) {
        RestTemplate rt = new RestTemplate();
        MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("st", st);
        requestEntity.add("app", "DEMO-APP");
        String response = rt.postForObject(USER_URL, requestEntity, String.class);
        if (!StringUtils.isEmpty(response)) {
            JSONObject jsonObject = JSON.parseObject(response);
            if (!StringUtils.isEmpty(jsonObject.getString("errtx"))) {
                return null;
            }
            if (StringUtils.isEmpty(jsonObject.getString("username"))) {
                return null;
            }

            UserObject userObject = new UserObject();
            userObject.setUsername(jsonObject.getString("username"));
            return userObject;
        }
        return null;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 解决跨域访问cookies的问题
        response.setHeader("P3P", "CP=CAO PSA OUR");

        // 获取当前登录用户
        UserObject userObject = getCurrentUserObject(request, response);
        if (userObject == null) {
            // 没有登录，则从请求参数中拿到ST
            String st = request.getParameter("st");
            if (StringUtils.isEmpty(st)) {
                // 没有ST，重定向到[sso-verify?app=&returnUrl=]去要一个ST
                redirect(VERIFY_URL, request, response);
                return;
            } else {
                // FIXME 有ST，验证ST合法性（访问[sso-validate?app=&st=]获取PT和登录用户信息）
                userObject = getAuthorizationUser(st);

                if (userObject == null) {
                    logger.warn(String.format("Client[%s] Attempt to access system use an invalidation token[st = %s]",
                            WebUtils.getIpAddress(request), st));

                    // ST无效，重定向到[sso-login?returnUrl=]去登录
                    redirect(LOGIN_URL, request, response);
                    return;
                } else {
                    // ST有效，保存用户信息
                    storeCurrentUserObject(request, response, userObject, PT_EXP);

                    // 记录日志
                    logger.info(String.format("用户[%s]认证成功，有效期%s秒", userObject.getUsername(), PT_EXP));
                }
            }
        }

        try {
            // 设置用户信息（Thread级别）
            UserContext.set(userObject);
            // 继续执行FliterChain
            filterChain.doFilter(request, response);
            return;
        } finally {
            // 清除用户信息
            UserContext.remove();
        }
    }

    /**
     * 获取当前登录用户
     */
    protected abstract UserObject getCurrentUserObject(HttpServletRequest request, HttpServletResponse response);

    /**
     * 保存当前登录用户
     *
     * @param userObject 登录用户信息
     * @param expires    过期时间
     */
    protected abstract void storeCurrentUserObject(HttpServletRequest request, HttpServletResponse response, UserObject userObject, int expires);

    protected void redirect(String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> extraParams = new HashMap<>();
        extraParams.put("app", "app");
        extraParams.put("rnd", RandomUtils.generateAlphaNumberSequence(8));
        // 如果有ST要去除st参数，防止循环想SSO服务器验证ST
        String urlToUse = encodeRetUrl(url, PARAM_RETURL, WebUtils.getRequestURLWithQueryString(request, "st"), extraParams);
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

}
