package io.summerx.sso.client.servlet;

import io.summerx.framework.cache.CustomizedCache;
import io.summerx.framework.utils.StringUtils;
import io.summerx.framework.web.utils.CookieUtils;
import io.summerx.sso.client.filter.SSOCacheFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 客户端（应用）监听SSO服务器
 *
 * 其实是一个Servlet，当SSO服务器执行Logout等操作时，将调用这个Servlet
 * 当然您首先需要在第一次向SSO服务器申请授权时附上这个Servlet的地址（SSOClientUserService）
 *
 * @author summerx
 * @Date 2016-07-14 3:38 PM
 */
public class CacheBasedSSOClientListener extends HttpServlet {

    // 存放用户信息的缓存
    private CustomizedCache userCache;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 移除缓存信息
        String ticket = request.getParameter("ticket");
        if (ticket == null) {
            ticket = CookieUtils.getCookieValue(request, SSOCacheFilter.COOKIE_PT);
        }
        if (userCache != null && !StringUtils.isEmpty(ticket)) {
            userCache.evict(ticket);
        }
        // 移除Cookie（由于有SSO服务器发起，故此段逻辑无实际作用）
        CookieUtils.removeCookie(SSOCacheFilter.COOKIE_PT, response);

        PrintWriter writer;
        try {
            response.setContentType("text/html; charset=" + "UTF-8");
            response.addHeader("Content-Type", "text/html");
            response.addHeader("charset", "UTF-8");
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Expires", "0");

            writer = response.getWriter();
            writer.print("Logout success.");
            // writer.close();
        } catch (IOException e) {
            throw e;
        }

    }

    public void setUserCache(CustomizedCache userCache) {
        this.userCache = userCache;
    }
}
