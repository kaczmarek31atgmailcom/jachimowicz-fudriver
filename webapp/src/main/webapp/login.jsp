<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page session="true" %>
<html>
<head>
    <title>Login Page</title>
    <title>Uniwersal</title>
    <link href="<c:url value="/css/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/css/fudriver.css" />" rel="stylesheet">
    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<body onload='document.loginForm.username.focus();'>

<script>
    history.pushState(null, null, location.href);
    window.onpopstate = function(event) {
        history.go(1);
    };
</script>

<div id="login-box" class="container">
    <h1><spring:message code="login.header"/></h1>
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">${error}</div>
    </c:if>
    <c:if test="${not empty msg}">
        <div class="alert alert-danger" role="alert">${msg}</div>
    </c:if>

    <form role="form" name="loginForm" action="<c:url value='/j_spring_security_check' />" method="POST">
        <div class="form-group">
            <label for="username"><spring:message code="login.username"/></label>
            <input type="text" class="form-control" id="username" name="username" placeholder="<spring:message code="login.username"/>">
        </div>
        <div class="form-group">
            <label for="password"><spring:message code="login.password"/></label>
            <input type="password" class="form-control" id="password" name="password" placeholder="<spring:message code="login.password"/>">
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary"><spring:message code="login.button"/></button>
    </form>


    </form>
</div>

</body>
</html>