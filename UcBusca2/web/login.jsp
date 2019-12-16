<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 10/12/2019
  Time: 13:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<header>
    <button id="home" onclick="window.location.href='index.jsp'">UcBusca</button>
</header>
<body>
<div class="container">
    <h1>Entrar</h1>
    <s:form action="login">
        <s:textfield id="inputUsername" name="username"><label>Username: </label></s:textfield>
        <s:textfield id="inputPassword" name="userpass"><label>Password: </label></s:textfield>
        <s:submit id="submit" value="Entrar"/>
        <s:submit id="submit" onclick="form.action='callback_facebook'" value="Login com Facebook" />
        <p class="member">Ainda não tens conta? <a href="register.jsp">Regista-te agora!</a>
    </s:form>
</div>
</body>
</html>
