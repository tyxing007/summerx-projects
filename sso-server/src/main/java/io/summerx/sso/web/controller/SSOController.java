package io.summerx.sso.web.controller;

import io.summerx.framework.utils.StringUtils;
import io.summerx.framework.web.utils.CookieUtils;
import io.summerx.framework.web.utils.WebClientInfo;
import io.summerx.sso.authentication.*;
import io.summerx.sso.authentication.exception.AuthenticationException;
import io.summerx.sso.constants.SSOServerContants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SSOController
 *
 * @author summerx
 * @Date 2016-06-16
 */
@Controller
@RequestMapping
public class SSOController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private boolean alwaysRedirect = false;

    @Resource(name = "cacheBasedAuthorizationManager")
    private AuthorizationManager authManager;

    /**
     * 登录
     */
    @RequestMapping(value = "/sso-login", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        // 返回URL
        String retUrl = request.getParameter(SSOServerContants.REQ_PARAM_RETURL);
        // Tgt
        String tgt = CookieUtils.getCookieValue(request, SSOServerContants.COOKIE_TGT);

        // 1. 若客户端持有一个TGT凭证，验证TGT凭证
        if (!StringUtils.isEmpty(tgt)) {
            UserAuthorization authorization = authManager.authenticate(new TgtCredentials(tgt, new WebClientInfo(request)));
            // 如果用户已经登录，则重定向到指定页面
            if (authorization != null) {
                return redirectIfLogin(authorization, retUrl);
            }
        }

        // 用户名，密码，验证码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String captcha = request.getParameter("captcha");
        // 2. 用户名密码都为null则直接显示登录页面
        if (username == null && password == null) {
            ModelAndView mv = new ModelAndView("auth/sso-login");
            // 保持retUrl
            mv.addObject("retUrl", retUrl);
            return mv;
        }

        try {
            // 3. 验证登录信息凭证
            final UsernamePasswordCredentials upc = new UsernamePasswordCredentials(username, password, captcha, new WebClientInfo(request));
            UserAuthorization authorization = authManager.authenticate(upc);

            // 4. 登录成功，通过Cookie将TGT凭证发送给客户端浏览器
            CookieUtils.addCookie(response, CookieUtils.createCookie(SSOServerContants.COOKIE_TGT, authorization.getTicket()), true);

            // 5. 跳转到该跳转的地方去
            return redirectIfLogin(authorization, retUrl);
        } catch (AuthenticationException ex) {
            // 日志记一下下
            logger.warn(ex.getMessage(), ex);

            ModelAndView mv = new ModelAndView("auth/sso-login");
            // 保持username
            mv.addObject("username", username);
            // FIXME 5. 验证失败，保存错误消息，返回登录页面
            mv.addObject("errtx", ex.getMessage());
            // 保持retUrl
            mv.addObject("retUrl", retUrl);
            return mv;

        }
    }

    /**
     * 核实用户是否已经登录，如果已经登录则颁发一个ST，用户可以拿着ST去访问应用
     */
    @RequestMapping(value = "/sso-verify", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView verify(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attrs) {
        // 用户试图访问的应用
        String app = request.getParameter(SSOServerContants.REQ_PARAM_APPNAME);
        String retUrl = request.getParameter(SSOServerContants.REQ_PARAM_RETURL);
        String tgt = CookieUtils.getCookieValue(request, SSOServerContants.COOKIE_TGT);

        // 1. 若客户端没有TGT，则显示登录页面
        if (StringUtils.isEmpty(tgt)) {
            return redirectIfNotLogin(retUrl, attrs);
        }

        // 2. 若客户端持有一个tgt则判断tgt是否有效
        final TgtCredentials tgtCredentials = new TgtCredentials(tgt, new WebClientInfo(request));
        // 验证TGT
        UserAuthorization authorization = authManager.authenticate(tgtCredentials);
        // 3. 没有登录，跳转到登录页面并附上原来的请求页面
        if (authorization == null) {
            return redirectIfNotLogin(retUrl, attrs);
        }

        // 3. 判断用户是否有权限访问应用
        if (!authManager.accessiable(authorization, app)) {
            // 尝试访问未经授权的应用
            logger.warn(String.format("User[%s - %s ]Attempt to access unauthorized apps[%s]", authorization.getUsername(), "FIXME", app));
            return new ModelAndView("err/auth-err");
        }

        // 4. 颁发一个ST凭证
        ProvisionalCredentials provisionalCredentials = authManager.issueProvisionalCredentials(authorization, app);

        // 5. 附上st，跳转到原来的请求
        attrs.addAttribute(SSOServerContants.REQ_PARAM_ST, provisionalCredentials.getTicket());
        return redirectIfLogin(authorization, retUrl);
    }

    /**
     * 验证st
     */
    @RequestMapping(value = "/sso-user", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validateSt(String app, String st, String url) {
        ModelAndView mv = new ModelAndView("__all-purpose");
        if (StringUtils.isEmpty(st) || StringUtils.isEmpty(app)) {
            // Error
            mv.addObject("errtx", "the argument is required; it must not be null");
            return mv;
        }

        final ProvisionalCredentials provisionalCredentials = new ProvisionalCredentials(st, app);
        provisionalCredentials.setUrl(url);
        // 验证Service Ticket
        UserAuthorization authorization = authManager.validateSt(provisionalCredentials);
        if (authorization == null) {
            // ST凭证无效或已经过期。
            mv.addObject("errtx", "ST凭证无效或已经过期");
            return mv;
        }

        // 用户
        mv.addObject("username", authorization.getUsername());
        // 应用
        mv.addObject("app", authorization.getApplication(app).getName());
        // Ticket
        mv.addObject("ticket", authorization.getApplication(app).getTikcet());
        return mv;
    }

    @RequestMapping(value = "/sso-logout", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {

        String tgt = CookieUtils.getCookieValue(request, SSOServerContants.COOKIE_TGT);
        if (tgt != null) {
            final TgtCredentials tgtCredentials = new TgtCredentials(tgt, new WebClientInfo(request));
            // 销毁tgt
            authManager.destoryTgt(tgtCredentials);
            // 清除客户端Cookie
            CookieUtils.removeCookie(SSOServerContants.COOKIE_TGT, response);
        }

        return new ModelAndView("auth/sso-login");
    }

    @RequestMapping(value = "/welcome", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("auth/welcome");
    }

    private ModelAndView redirectIfLogin(UserAuthorization authorization, String retUrl) {
        ModelAndView mv;
        if (!alwaysRedirect && !StringUtils.isEmpty(retUrl)) {
            // FIXME 白名单url则跳转到上一次用户请求页面
            mv = new ModelAndView("redirect:" + retUrl);
        } else {
            // 跳转到指定页面（如：首页）
            mv = new ModelAndView("redirect:welcome");
        }
        return mv;
    }

    private ModelAndView redirectIfNotLogin(String retUrl, RedirectAttributes attrs) {
        ModelAndView mv = new ModelAndView("redirect:sso-login");
        attrs.addAttribute(SSOServerContants.REQ_PARAM_RETURL, retUrl);
        return mv;
    }
}
