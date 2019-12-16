<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 08/12/2019
  Time: 12:45
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Histórico de pesquisa</title>
    <link rel="stylesheet" type="text/css" href="css/historico.css">
</head>
<c:choose>
    <c:when test="${session.Admin == true}">
        <jsp:forward page="/historicoadmin.jsp"></jsp:forward>
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
<h1>Histórico de pesquisas</h1>
<div id="container"><div id="history">
    <c:forEach items="${BuscaBean.getHistoricopesquisa()}" var="value">
        <c:out value="${value}" /><br>
    </c:forEach>
</div></div>
</body>
</html>
