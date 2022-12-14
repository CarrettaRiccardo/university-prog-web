<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!--
    Contiene tutti i riferimenti a style o js.
    Definisce anche tutti gli script comuni
-->
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">


<%-- Configurazione base path, per non inserire "/project" in ogni link --%>
<base href="/project/"/>
<link href="assets/favicon.ico" rel="icon" type="image/x-icon"/>

<%-- Bootstrap --%>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.0/jquery-ui.min.js"
        integrity="sha256-eGE6blurk5sHj+rmkfsGYeKyZx3M4bG+ZlFyA7Kns7E="
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
      integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<script src="https://unpkg.com/gijgo@1.9.13/js/gijgo.min.js" type="text/javascript"></script>
<link href="https://unpkg.com/gijgo@1.9.13/css/gijgo.min.css" rel="stylesheet" type="text/css"/>

<script src="bootstrap/js/bootstrap.min.js"></script>

<%-- CSS globale (contiene bootstrap)--%>
<link rel="stylesheet" href="css/theme.css">

<%-- DataTables --%>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/bs4/dt-1.10.18/r-2.2.2/datatables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/v/bs4/dt-1.10.18/r-2.2.2/datatables.min.js"></script>

<%-- Typehead  --%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-3-typeahead/4.0.2/bootstrap3-typeahead.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/css/select2.min.css" rel="stylesheet"/>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/js/select2.js"></script>

<script>
    function getCookie(c_name) {
        var c_value = document.cookie,
            c_start = c_value.indexOf(" " + c_name + "=");
        if (c_start == -1) c_start = c_value.indexOf(c_name + "=");
        if (c_start == -1) {
            c_value = null;
        } else {
            c_start = c_value.indexOf("=", c_start) + 1;
            var c_end = c_value.indexOf(";", c_start);
            if (c_end == -1) {
                c_end = c_value.length;
            }
            c_value = unescape(c_value.substring(c_start, c_end));
        }
        return c_value;
    }

    $(document).ready(function () {
        var tokenCookie = getCookie("acceptCookies");
        if (tokenCookie) {
            document.getElementById("bannerCookies").style.display = "none";
        } else {
            document.getElementById("bannerCookies").style.display = "block";
        }
    });

    $(document).ready(function () {
        $('#closeCookies').click(function () {
            document.getElementById("bannerCookies").style.display = "none";

            var now = new Date();
            document.cookie = "acceptCookies=true; max-age=" + 60 * 60 * 24 * 90 + "; path=/";// valido per 90 giorni
        });
    });

    jQuery(document).ready(function ($) {
        <c:if test="${! empty url_rest}">    <!-- Codice per ricerca con autocompletamento -->
            $("#autocomplete").select2({
                placeholder: "Digita un nome",
                allowClear: true,
                disabled: false,
                ajax: {
                    url: function (params) {
                        return "${url_rest}" + params.term;

                    },
                },
                dataType: "json"

            });
            $("#autocomplete").val(null).trigger("change");
        </c:if>
    });
</script>
    
<script type="text/javascript">
        $(document).ready(function () {
            $('#table').DataTable({
                // Autocomletamento con typeahead
                initComplete: function () {
                    let dataset = [];
                    let api = this.api();
                    // Popola il dataset prendendo i dati direttamente dalla tabella
                    // Usa le colonne 0, 1 e 2 -> TODO: Usare le colonne giuste in base alla sezione
                    api.cells('tr', [0, 1]).every(function () {
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
                        renderer: $.fn.dataTable.Responsive.renderer.listHidden({
                            tableClass: 'table'
                        })
                    }
                },
                // Disabilitazione ordinamento automatico
                order: []
            })
            // Gestione click row table
                .on('click', 'tbody tr[data-href] td:nth-child(1)', function (e) {
                    let pseudo = window.getComputedStyle($(this).get(0), ':before');
                    if (pseudo.getPropertyValue("content") !== 'none' && // Controllo che ci sia il bottone
                        e.target.nodeName === 'TD' &&  // Che abbia premuto la riga e non un elmento all'interno
                        e.offsetX < 30) { // Che abbia premuto sul bottone e non sul testo dopo
                        // Blocco la propagazione dell'evento perch?? ho premuto il bottone "+"
                        e.stopPropagation();
                    } else {
                        // Altrimenti ho premuto sulla riga della tabella, chiudo eventuali dettagli aperti
                        $('tr.child').hide();
                        $('tr.parent').removeClass('parent');
                    }
                })
                // Gestione click row table
                .on('click', 'tbody tr[data-href]', function (e) {
                    window.location.href = $(this).attr('data-href');
                });
        });
</script>

<div id="bannerCookies" style="display: none" class="alert alert-dismissible my-0 bg-secondary fixed-bottom text-center"
     role="alert">
    <div>
        <fmt:message key="lbl_footer"/> <a href="http://www.whatarecookies.com/" class="text-light mx-2" target="_blank"><fmt:message key="about_cookie"/></a>
        <button id="closeCookies" type="button" class="btn bg-dark text-light btn-sm">
            <fmt:message key="chiudi"/>
        </button>
    </div>
</div>