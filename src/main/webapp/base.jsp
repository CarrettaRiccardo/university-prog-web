<%-- Pagina di base per tutte le categorie --%>
<%@ include file="global/common.jsp" %>
<html>
<head>
    <title>
        <c:out value="${title}"/>
    </title>
    <%@include file="global/head.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#table').DataTable({
                pageLength: 50,
                responsive: {
                    details: {
                        display: $.fn.dataTable.Responsive.display.modal({
                            header: function (row) {
                                var data = row.data();
                                return 'Dettagli per ' + data[0] + ' ' + data[1];
                            }
                        }),
                        renderer: $.fn.dataTable.Responsive.renderer.tableAll({
                            tableClass: 'table'
                        })
                    }
                }
            });
        });
    </script>
</head>
<body>
<jsp:include page="/global/navbar.jsp">
    <jsp:param name="showMenu" value="true"/>
</jsp:include>

<div class="container-fluid pt-2">
    <div class="row">
        <div class="col-12 col-md-2">
            <jsp:include page="/components/navigation.jsp"/>
        </div>
        <div class="col-12 col-md-10">
            <h2 class="mt-2 mb-4">${title}</h2>
            <jsp:include page="/components/${page}.jsp"/>
            <%--
            <c:choose>
                <c:when test="${page == 'prenotazioni'}">
                    <jsp:include page="/components/prenotazioni.jsp"/>
                </c:when>
                <c:when test="${page == 'visite'}">
                    <jsp:include page="/components/visite.jsp"/>
                </c:when>
                <c:when test="${page == 'visite_specialistiche'}">
                    <jsp:include page="/components/visite.jsp"/>
                </c:when>
                <c:otherwise>
                    <div>ERROR</div>
                </c:otherwise>
            </c:choose>
            --%>
        </div>
    </div>
</div>
<jsp:include page="/global/footer.jsp"/>
</body>
</html>
