<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 10/12/2019
  Time: 13:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registar</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<header>
    <button id="home" onclick="window.location.href='index.jsp'">UcBusca</button>
</header>
<body>
<div class="container">
    <h1>Registar</h1>
    <s:form action="register">
        <s:textfield id="inputUsername" name="username"><label xml:space="preserve">Username: </label></s:textfield>
        <s:textfield type="password" id="inputPassword" name="userpass"><label xml:space="preserve">Password: </label></s:textfield>
        <s:submit value="Registar"></s:submit>
    </s:form>
</div>
</body>
</html>
