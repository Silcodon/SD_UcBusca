<%--
  Created by IntelliJ IDEA.
  User: anton
  Date: 06/12/2019
  Time: 20:41
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>UcBusca</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
  </head>
  <header>
    <button id="home" onclick="window.location.href='index.jsp'">UcBusca</button>
    <button id="login" onclick="window.location.href='login.jsp'">Login</button>
  </header>
  <body>
      <img src="assets/UcBusca.png" id="logo">
      <s:form action="buscar" method="post">
          <s:textfield name="buscaBean.termopesquisa" placeholder="Procure aqui..." id="search" />
          <s:submit id="procura" value="Procure"/>
      </s:form>
  </body>
</html>
