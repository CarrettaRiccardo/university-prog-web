<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">


<%-- Configurazione base path, per non inserire "/project" in ogni link --%>
<base href="/project/"/>

<%-- Bootstrap --%>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
      integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
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
        $(".clickable-row").click(function () {
            window.location = $(this).data("href");
        });
        
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

<div id="bannerCookies" style="display: none" class="alert alert-dismissible my-0 bg-secondary fixed-bottom text-center"
     role="alert">
    <div>
        <b>Ti piacciono i cookies?</b> &#x1F36A; Se utilizzi il nostro sito accetti l'utilizzo di cookie per migliorare
        la navigazione. <a href="http://www.whatarecookies.com/" class="text-light mx-2" target="_blank">Per saperne di
        piu'</a>
        <button id="closeCookies" type="button" class="btn bg-dark text-light btn-sm">
            Chiudi
        </button>
    </div>
</div>