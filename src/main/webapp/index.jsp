<%@ include file="global/common.jsp" %>
<%@ page pageEncoding="UTF-8" %>
<html>

<head>
    <title>SanityManager</title>

    <%@include file="global/head.jsp" %>
    <link rel="stylesheet" href="css/index.css">

    <script>
        $(document).ready(function () {
            $('#btnLogin').click(function () {
                window.location.href = "login"
            });

            $('#show_more_btn').click(function () {
                $([document.documentElement, document.body]).animate({
                    scrollTop: $("#scroll_target").offset().top
                }, {
                    duration: 1000,
                    easing: 'easeOutExpo'
                });
            });

            // Animazione counts
            function setAnimation($el, count) {
                $({count: 0}).animate({
                    count: count
                }, {
                    duration: 2000,
                    easing: 'easeOutExpo',
                    step: function () {
                        $el.text(commaSeparateNumber(Math.round(this.count)));
                    }
                });

                function commaSeparateNumber(val) {
                    while (/(\d+)(\d{3})/.test(val.toString())) {
                        val = val.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
                    }
                    return val;
                }
            }

            setAnimation($('#pazienti_count'), 200);
            setAnimation($('#medici_count'), 30);
            setAnimation($('#ssp_count'), 110);
            setAnimation($('#prescrizioni_count'), 4002);
        });
    </script>
</head>

<body class="bg">

<nav class="navbar">
    <a class="navbar-brand ">
        <img src="assets/logo.svg" width="36" height="36" class="d-inline-block align-top mr-2" alt="logo">
        <span class="h4 font-weight-normal d-none d-sm-inline">
            <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
        </span>
    </a>
    <button id="btnLogin" class="btn btn-light rounded-pill font-weight-bold px-5 shadow">Accedi</button>
</nav>

<div class="container-fluid mt-5 mb-5 pb-5">
    <div class="row text-center pt-lg-5 mb-lg-5">
        <div class="col-12 col-lg-5 pt-md-4">
            <img src="assets/logo.svg" width="128" height="128"/>
            <div class="display-4 pb-2 font-weight-normal">
                <h1>
                    <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
                </h1>
            </div>
            <p class="h3 font-weight-lighter text-muted">La rivoluzione nel campo della sanità</p>
        </div>
        <div class="col-12 col-lg-7 pl-lg-5 pt-4 mt-5 mt-lg-0">
            <div class="text-black m-auto" style="max-width: 500px">
                <h1 class="font-weight-bold mb-4">Gestisci tutte le tue pratiche in unico sistema</h1>
                <div class="h5">
                    SanityManager è l'innovativa applicazione Web per gestire in modo semplice e sicuro le
                    pratiche relative alla sanità pubblica.<br/><br/>
                    SanityManager è rivolta a pazienti, medici e servizi sanitari provinciali.
                </div>
            </div>
        </div>
    </div>

    <div class="row pt-4 pb-3 text-center ">
        <div class="col-12 col-sm-6 col-lg-3">
            <h6 class="bg-light rounded-pill px-5 py-1 shadow mx-auto d-sm-block" style="max-width: 260px;">
                <span id="pazienti_count" class="font-weight-bold h3">100</span><br/>pazienti registrati</h6>
        </div>
        <div class="col-12 col-sm-6 col-lg-3">
            <h6 class="bg-light rounded-pill px-5 py-1 shadow mx-auto" style="max-width: 260px;">
                <span id="medici_count" class="font-weight-bold h3">100</span><br/>medici registrati</h6>
        </div>
        <div class="col-12 col-sm-6 col-lg-3">
            <h6 class="bg-light rounded-pill px-5 py-1 shadow mx-auto" style="max-width: 260px;">
                <span id="ssp_count" class="font-weight-bold h3">100</span><br/>ssp registrati</h6>
        </div>
        <div class="col-12 col-sm-6 col-lg-3">
            <h6 class="bg-light rounded-pill px-5 py-1 shadow mx-auto" style="max-width: 260px;">
                <span id="prescrizioni_count" class="font-weight-bold h3">100</span><br/>prescrizioni effettuate</h6>
        </div>
    </div>

    <div id="show_more_btn" class="row pt-3 mb-5 text-center d-none d-lg-flex" style="cursor: pointer">
        <div class="col-12">
            <div>Scopri tutte le funzionalità</div>
            <div class="display-3" style="line-height: 1rem">&#8964;</div>
        </div>
    </div>

    <div id="scroll_target"></div>

    <div class="row pt-5 px-md-5">
        <div class="col-12 col-lg-6 pa-2 text-center">
            <div class="display-4 pb-1 text-gradient-1">Servizi per gli Utenti</div>
            <p class="lead">
                <b class="text-gradient-1">SanityManager</b> permette di risparmiare tempo prenotando
                <b class="text-gradient-1">direttamente da casa</b> una visita dal medico di base.<br/>
                Ogni utente può visualizzare tutte le <b class="text-gradient-1">visite</b> fatte, avendo accesso anche
                ai referti di esami e visite specialistiche.<br/>
                È inoltre possibile visualizzare e stampare le proprie <b class="text-gradient-1">ricette</b>
                direttamenta dal sito web.
            </p>
        </div>
        <div class="col-12 col-lg-6"></div>
    </div>


    <div class="row px-md-5 mt-5 mt-lg-0">
        <div class="col-12 col-lg-6"></div>
        <div class="col-12 col-lg-6 pa-2 text-center">
            <div class="display-4 pb-1 text-gradient-2">Servizi per i Medici di base</div>
            <p class="lead">
                <b class="text-gradient-2">SanityManager</b> permette di <b class="text-gradient-2">compilare le
                visite</b> direttamente online e di prescrivere visite specialistiche,
                esami e ricette in modo semplice e veloce. <br/>
                Ogni medico è in grado di visualizzare tutti i <b class="text-gradient-2">referti</b> dei suoi pazienti
                per visite specialistiche ed esami passati.
            </p>
        </div>
    </div>


    <div class="row px-md-5 mt-5 mt-lg-0">
        <div class="col-12 col-lg-6 pa-2 text-center">
            <div class="display-4 pb-1 text-gradient-3">Servizi per i Medici specialistici</div>
            <p class="lead">
                <b class="text-gradient-3">SanityManager</b> permette di <b class="text-gradient-3">compilare online le
                visite</b> specialistiche.
                Ogni medico ha accesso ai dati dell'utente e alle passate visite per un'analisi più accurata della
                storia delle malattie del paziente.
            </p>
        </div>
        <div class="col-12 col-lg-6"></div>
    </div>


    <div class="row px-md-5 mt-5 mt-lg-0">
        <div class="col-12 col-lg-6"></div>
        <div class="col-12 col-lg-6 pa-2 text-center">
            <div class="display-4 pb-1 text-gradient-5">Servizi per i SSP</div>
            <p class="lead">
                <b class="text-gradient-5">SanityManager</b> permette di <b class="text-gradient-5">inserire online i
                risultati relativi agli esami svolti dai pazienti.</b> <br/>
                Ogni SSP ha inoltre accesso ad una serie di report territoriali per avere una visione generale
                sulle attività svolte.
            </p>
        </div>
    </div>

</div>
<jsp:include page="/global/footer.jsp"/>
</body>
</html>
