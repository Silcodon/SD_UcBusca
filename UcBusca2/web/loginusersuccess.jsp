<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 11/12/2019
  Time: 12:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Sucesso</title>
    <link rel="stylesheet" type="text/css" href="css/pesquisauser.css">
</head>
<c:choose>
    <c:when test="${session.Admin == true}">
        <jsp:forward page="/indexadmin.jsp"></jsp:forward>
    </c:when>
    <c:when test="${session.User == true}">
    </c:when>
    <c:otherwise>
        <jsp:forward page="/login.jsp"></jsp:forward>
    </c:otherwise>
</c:choose>
<header>
    <button id="home" onclick="window.location.href='indexuser.jsp'">UcBusca</button>
    <s:form action="historicouser">
        <s:submit cssClass="button" id="historico" value="Historico de pesquisa"/>
    </s:form>
    <s:form action="logout">
        <s:submit cssClass="button" id="login" value="Sair"/>
    </s:form>
</header>
<body>

<div id="container">
<p>Login com sucesso!</p>
    <br/>Bem-vindo, <s:property value="username"/></div>
</body>
</html>
