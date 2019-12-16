<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 08/12/2019
  Time: 15:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Informação do Sistema</title>
    <link rel="stylesheet" type="text/css" href="css/informacaosistema.css">
</head>
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
<header>
    <button id="home" onclick="window.location.href='indexadmin.jsp'">UcBusca</button>
    <button id="admin" onclick="window.location.href='adicionaadmin.jsp'">Adicionar admin</button>
    <button id="indexar" onclick="window.location.href='indexarurl.jsp'">Indexar URL</button>

    <s:form action="historicoadmin">
        <s:submit cssClass="button" id="historico" value="Historico de pesquisa"/>
    </s:form>
    <button id="informacao" onclick="window.location.href='informacaosistema.jsp'">Informação do Sistema</button>
    <s:form action="logout">
        <s:submit id="login" value="Sair"/>
    </s:form>
</header>
<body>
<h1>Informação do Sistema</h1>
<div id="maispesquisadas">
    <h2>Palavras mais pesquisadas</h2>
    <c:forEach items="${BuscaBean.getInformacao()}" var="value">
        <c:out value="${value}" /><br>
    </c:forEach>
</div>
<div id="informacoes">
    <h2>Sistema</h2>

</div>
</body>
</html>
