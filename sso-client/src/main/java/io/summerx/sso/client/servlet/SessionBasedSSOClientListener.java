package io.summerx.sso.client.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 客户端（应用）监听SSO服务器
 *
 * 其实是一个Servlet，当SSO服务器执行Logout等操作时，将调用这个Servlet
 * 当然您首先需要在第一次向SSO服务器申请授权时附上这个Servlet的地址（SSOClientUserService）
 *
 * @author summerx
 * @Date 2016-07-14 3:38 PM
 */
public class SessionBasedSSOClientListener extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 清除session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
