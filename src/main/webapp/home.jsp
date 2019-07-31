<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>
    <title>Home</title>

    <%@include file="global/head.jsp" %>
</head>

<body>
<jsp:include page="global/navbar.jsp"/>

<div class="container pt-2">
    <div class="row">
        <c:forTokens items="Visite,Visite specialistiche,Esami,Ricette" var="cat" varStatus="s" delims=",">
            <div class="col-lg-6 p-1">
                <a class="btn btn-block btn-lg btn-gradient text-light bg-gradient-<c:out value="${s.index + 1}"/>">
                    <span class="h5 font-weight-normal"><c:out value="${cat}"/></span>
                </a>
            </div>
        </c:forTokens>
    </div>
</div>
</body>

</html>
