<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%-- 为搜索引擎提供的关键字列表 --%>
    <meta name="keywords" content="跨境电商,CBE"/>
    <%-- 告诉搜索引擎你的网站是干嘛的 --%>
    <meta name="description" content="跨境电商综合服务平台"/>
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/>
    <link rel="BookMark" href="/favicon.ico" type="image/x-icon"/>
    <title>跨境电商综合服务平台</title>
</head>
<style>
    body {
    }
    .login-logo {
        margin-top: 0px;
    }
    .login-info {
        float: left;
        margin-right: 80px;
    }
    .login-form {
        float: left;
    }
</style>
<body>
<div class="header" style="height: 100px">
</div>
<div class="content" style="margin: 0 auto; width: 960px;">
    <div class="login-logo">
        <h1>
            <a href="http://www.infocbe.com/">
                <img border="0" alt="跨境电商综合服务平台" src="http://try.infoccsp.com/static/nav-logo_zh.png">
            </a>
        </h1>
    </div>
    <div class="login-main">
        <div class="login-info">
            <div><img alt="航空物流信息服务平台" src="http://try.infoccsp.com/static/login-info.jpg"></div>
        </div>
        <div class="login-form">
            <h3 class="login-form-title">用户登录</h3>
            <form action="/sso/sso-login" method="post">
                <span style="color:red;">${errtx}</span><br/>
                <input type="hidden" name="retUrl" value="${retUrl}">
                Username: <input type="text" name="username" value="${username}"><br/>
                Password: <input type="password" name="password" value=""><br>
                <input type="submit" value="Login">
            </form>
        </div>
    </div>
</div>
<div class="footer">

</div>
</body>
</html>