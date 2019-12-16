<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 12/12/2019
  Time: 16:17
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UcBusca</title>
    <link rel="stylesheet" type="text/css" href="css/pesquisauser.css">
</head>
<c:choose>
    <c:when test="${session.Admin == true}">
        <jsp:forward page="/pesquisa_urladmin.jsp"></jsp:forward>
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
        <s:submit id="login" value="Sair"/>
    </s:form>
</header>
<body>
<h1>Resultados da pesquisa do URL <c:out value="${buscaBean.termopesquisa}"/></h1>
<div id="container"><div id="resultados">
    <c:forEach items="${buscaBean.resultados_url}" var="value">
        <c:out value="${value}" /><br>
    </c:forEach>
</div></div>
</body>
</html>
