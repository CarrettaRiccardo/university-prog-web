<%-- Pagina di base per tutte le categorie --%>
<%@ include file="global/common.jsp" %>
<html>
<head>
    <title>
        <fmt:message key="${title}"/>
    </title>
    <%@include file="global/head.jsp" %>
</head>
<body>
<jsp:include page="/global/navbar.jsp"/>

<div class="container pt-2 min-vh-100">
    <div class="row">
        <div class="col-12">
            <form action="app/download" method="POST">
                <h2 class="mt-2 mb-4 d-print-none"><fmt:message key="${title}"/> ${nome}
                    <c:if test="${utente.isSsp() && page=='esami'}"><button name="download" class="float-right btn btn-gradient"><fmt:message key="download_odierno"/></button></c:if>
                </h2>
            </form>
            <jsp:include page="/components/${page}.jsp"/>
        </div>
    </div>
</div>
<jsp:include page="/global/footer.jsp"/>
</body>
</html>
