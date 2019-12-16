<%@ page import="Meta2.action.BuscaAction" %><%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 07/12/2019
  Time: 23:15
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>UcBusca</title>
    <link rel="stylesheet" type="text/css" href="css/styleadmin.css">
</head>
<header>
    <button id="home" onclick="window.location.href='indexadmin.jsp'">UcBusca</button>
    <button id="admin" onclick="window.location.href='adicionaadmin.jsp'">Adicionar admin</button>
    <button id="indexar" onclick="window.location.href='indexarurl.jsp'">Indexar URL</button>
    <s:form action="historicoadmin">
        <s:submit cssClass="button" id="historico" value="Historico de pesquisa"/>
    </s:form>
    <button id="informacao" onclick="window.location.href='informacaosistema.jsp'">Informação do Sistema</button>
    <s:form action="logout">
        <s:submit  id="login" value="Sair"/>
    </s:form>
</header>
<body>
<c:choose>
    <c:when test="${session.Admin == true}">
    </c:when>
    <c:when test="${session.User == true}">
        <jsp:forward page="/indexuser.jsp"></jsp:forward>
    </c:when>
    <c:otherwise>
        <jsp:forward page="/login.jsp"></jsp:forward>
    </c:otherwise>
</c:choose>
<img src="assets/UcBusca.png" id="logo">
<s:form action="buscaradmin" method="post">
    <s:textfield name="buscaBean.termopesquisa" placeholder="Procure aqui..." id="search" />
    <s:submit id="procura" value="Procure"/>
    <s:submit id="procura2" value="Procure URL" onclick="form.action='buscarurladmin'"/>
</s:form>
</body>
</html>
