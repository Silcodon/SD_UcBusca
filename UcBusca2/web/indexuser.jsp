<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 08/12/2019
  Time: 00:00
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>UsBusca</title>
    <link rel="stylesheet" type="text/css" href="css/styleuser.css">
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
<img src="assets/UcBusca.png" id="logo">
<s:form action="buscaruser" method="post">
    <s:textfield name="buscaBean.termopesquisa" placeholder="Procure aqui..." id="search" />
    <s:submit id="procura" value="Procure"/>
    <s:submit id="procura2" value="Procure URL" onclick="form.action='buscarurluser'"/>
</s:form>
</body>
</html>
