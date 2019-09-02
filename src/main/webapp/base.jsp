<%-- Pagina di base per tutte le categorie --%>
<%@ include file="global/common.jsp" %>
<html>
<head>
    <title>
        <fmt:message key="${title}"/>
    </title>
    <%@include file="global/head.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#table').DataTable({
                // Autocomletamento con typeahead
                initComplete: function () {
                    let dataset = [];
                    let api = this.api();
                    // Popola il dataset prendendo i dati direttamente dalla tabella
                    // Usa le colonne 0, 1 e 2 -> TODO: Usare le colonne giuste in base alla sezione
                    api.cells('tr', [0, 1, 2]).every(function () {
                        // Rimozione leemnti HTML per avere solo il testo. Non insersco duplicati
                        let data = $('<div>').html(this.data()).text();
                        if (dataset.indexOf(data) === -1) dataset.push(data);
                    });
                    // Ordinamento dataset
                    dataset.sort();
                    // Inizializzazione Select2
                    let select = $('#table_filter input[type="search"]', api.table().container());
                    select.typeahead({
                        source: dataset,
                        afterSelect: function (value) {
                            api.search(value).draw();
                        }
                    });
                },
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
                },
                // Disabilitazione ordinamento automatico
                order: []
            });
        });
    </script>
</head>
<body>
<jsp:include page="/global/navbar.jsp"/>

<div class="container pt-2 min-vh-100">
    <div class="row">
        <div class="col-12">
            <h2 class="mt-2 mb-4"><fmt:message key="${title}"/>${nome}</h2>
            <jsp:include page="/components/${page}.jsp"/>
        </div>
    </div>
</div>
<jsp:include page="/global/footer.jsp"/>
</body>
</html>
