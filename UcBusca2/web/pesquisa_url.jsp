<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 12/12/2019
  Time: 16:16
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UcBusca</title>
    <link rel="stylesheet" type="text/css" href="css/pesquisa.css">
</head>
<header>
    <button id="home" onclick="window.location.href='index.jsp'">UcBusca</button>
    <button id="login" onclick="window.location.href='login.jsp'">Login</button>
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
