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
                // Traduzione componenti DataTables
                language: {
                    search: "Cerca:",
                    emptyTable: "Nessun elemento disponibile",
                    info: "Elementi da _START_ a _END_ di _TOTAL_ totali",
                    infoEmpty: "0 elementi totali",
                    infoFiltered: "(filtrati da _MAX_ elementi totali)",
                    lengthMenu: "Mostra _MENU_ elementi",
                    loadingRecords: "Caricamento...",
                    processing: "Calcolo...",
                    zeroRecords: "Nessun elemento trovato",
                    paginate: {
                        first: "Primo",
                        last: "Ultimo",
                        next: "Prossimo",
                        previous: "Precedente"
                    },
                },
                responsive: {
                    details: {
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

<div class="container pt-2 min-vh-100">
    <div class="row">
        <div class="col-12 col-md-2">
            <jsp:include page="/components/navigation.jsp"/>
        </div>
        <div class="col-12 col-md-10">
            <h2 class="mt-2 mb-4"><fmt:message key="${title}"/> ${nome} </h2>
            <jsp:include page="/components/${page}.jsp"/>
        </div>
    </div>
</div>
<jsp:include page="/global/footer.jsp"/>
</body>
</html>
