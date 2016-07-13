<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login</title>
</head>
<body>
<form action="/sso/sso-auth" method="post">
    <input type="hidden" name="retUrl" value="${retUrl}">
    Username: <input type="text" name="username" value="admin"><br/>
    Password: <input type="password" name="password" value="admin123"><br>
    <input type="submit" value="Login">
</form>
</body>
</html>