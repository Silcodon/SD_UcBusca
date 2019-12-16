<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 10/12/2019
  Time: 20:19
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UcBusca</title>
    <meta property="og:url"           content="http://localhost:8008/UcBusca2/buscaradmin.action" />
    <meta property="og:type"          content="website" />
    <meta property="og:title"         content="UcBusca" />
    <meta property="og:description"   content="Partilha de pesquisa" />
    <meta property="og:image"         content="http://localhost:8008/UcBusca2/assets/UcBusca.png" />
    <link rel="stylesheet" type="text/css" href="css/pesquisaadmin.css">
</head>
<c:choose>
    <c:when test="${session.Admin == true}">
    </c:when>
    <c:when test="${session.User == true}">
        <jsp:forward page="/pesquisauser.jsp"></jsp:forward>
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
<!-- Load Facebook SDK for JavaScript -->
<div id="fb-root"></div>
<script>(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v3.0";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>

<h1>Resultados da pesquisa <c:out value="${buscaBean.termopesquisa}"/></h1>
<div id="container"><div id="resultados">

    <!-- Your share button code -->
    <div class="fb-share-button"
         data-href="http://localhost:8008/UcBusca2/buscaradmin.action"
         data-layout="button_count">
    </div>
    <s:form action="translateadmin">
        <s:submit value="Traduzir"/>
    </s:form>
    <c:forEach items="${buscaBean.resultados}" var="value">
        <c:forTokens items="${value}" delims="|XXX|" var="value2">
            <c:out value="${value2}"/>
            <p white-space: pre-wrap; ></p>
        </c:forTokens>
        <br />


    </c:forEach>
</div></div>
</body>
</html>

