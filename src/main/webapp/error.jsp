<%@ include file="global/common.jsp" %>
<%@ page isErrorPage="true" %>
<html>
<head>
    <title>Error</title>

    <%@include file="global/head.jsp" %>
    <%-- Questo path rimane uguale perchè valutato a compile time--%>
    <link rel="stylesheet" href="css/error.css">
    <%-- La pagina di login è mappata su /app/login. Quindi mettere solo scc/login dà not found perchè cerca in /app/scss e non trova nulla--%>
</head>
<!-- RICKY -->
<body class="bg">

<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm d-print-none">
    <a class="navbar-brand" href="app/home">
        <img src="assets/logo.svg" width="36" height="36" class="d-inline-block align-top mr-2" alt="logo">
        <span class="h4 font-weight-normal d-none d-lg-inline">
            <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
        </span>
    </a>
</nav>

<div class="container">
    <div class="display-4 pb-1 text-center font-weight-normal mt-5">
        <img src="assets/ic_alert.svg" width="110" height="110"/>
        <span class="text-gradient-6 mb-5">Sanity</span><span class="font-weight-light">Manager</span>
    </div>
    <div id="messageBlock" class="text-center container-fluid">
        <div class="alert alert-danger text-center mt-4">
            <c:if test="${pageContext.exception.message == null}">
                <c:out value="${pageContext.errorData.statusCode}"/>
                <c:out value=":"/>
                <c:choose>
                    <c:when test="${pageContext.errorData.statusCode == '400'}"> <fmt:message key="error_code_400"/>
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == '401'}"> <fmt:message key="error_code_401"/>
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == '403'}"> <fmt:message key="error_code_403"/>
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == '404'}"> <fmt:message key="error_code_404"/>
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == '500'}"> <fmt:message key="error_code_500"/>
                    </c:when>
                    <c:otherwise> <fmt:message key="error_code_other"/> </c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${pageContext.exception.message != null}">
                <c:out value="${pageContext.exception.message}"/>
            </c:if>
        </div>
        <div class="alert alert-dark text-center my-3">
            <c:if test="${pageContext.exception.message == null}">
                <c:choose>
                    <c:when test="${pageContext.errorData.statusCode == '400'}"> <fmt:message key="error_message_400"/>
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == '401'}"> <fmt:message key="error_message_401"/>
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == '403'}"> <fmt:message key="error_message_403"/>
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == '404'}"> <fmt:message key="error_message_404"/>
                    </c:when>
                    <c:when test="${pageContext.errorData.statusCode == '500'}"> <fmt:message key="error_message_500"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="error_message_other"/>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${pageContext.exception.message != null}">
                <fmt:message key="${pageContext.exception.message}"/>
            </c:if>
        </div>
        <a id="btnHome" class="btn btn-lg btn-gradient rounded-pill mt-5" href="app/home">Torna alla Home</a>
    </div>
</div>
</body>
</html>