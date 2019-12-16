<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 14/12/2019
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Pesquisa</title>
    <link rel="stylesheet" type="text/css" href="css/pesquisa.css">
</head>
<header>
    <button id="home" onclick="window.location.href='index.jsp'">UcBusca</button>
    <button id="login" onclick="window.location.href='login.jsp'">Login</button>
</header>
<body>
<h1>Resultados da pesquisa <c:out value="${buscaBean.termopesquisa}"/></h1>
<div id="container"><div id="resultados">
    <c:forEach items="${buscaBean.resultadostraduzidos}" var="value">
        <c:forTokens items="${value}" delims="|XXX|" var="value2">
            <c:out value="${value2}"/>
            <p white-space: pre-wrap; ></p>
        </c:forTokens>
        <br />


    </c:forEach>
    Powered by <a href="https://translate.yandex.com/">Yandex.Translate</a>
</div></div>
</body>
</html>
