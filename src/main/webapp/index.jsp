<%@ include file="global/common.jsp" %>
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

<div class="container-fluid mt-5">
    <div class="row text-center pt-md-5">
        <div class="col-12 col-md-5 pt-md-5">
            <img src="assets/logo.svg" width="128" height="128"/>
            <div class="display-4 pb-2 font-weight-normal">
                <h1>
                    <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
                </h1>
            </div>
            <p class="h3 font-weight-lighter text-muted">La rivoluzione nel campo della sanità</p>
        </div>
        <div class="col-12 col-md-7 pl-md-5 pt-5">
            <div class="text-black m-auto" style="max-width: 500px">
                <div class="display-4 font-weight-bold mb-4">Titolo qualcosa</div>
                <div>
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore
                    et
                    dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut
                    aliquip
                    ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum
                    dolore eu
                    fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia
                    deserunt mollit anim id est laborum.
                </div>
            </div>
        </div>
    </div>

    <div class="row d-none d-md-block">
        <div class="col-12 h-25">
            <h1>La nostra Applicazione</h1>
            <p>
                SanityManager è l'innovativa Applicazione Web per gestire in modo semplice e sicuro le 
                pratiche relative alla sanità pubblica. <br/>
                SanityManager è rivolta ai singoli pazienti, ma anche a tutti i medici e servizi sanitari provinciali.
            </p>
        </div>
    </div>
    
    <div class="row d-none d-md-block text-center">
        <div class="col-6 h-25">
            <h2>Servizi per gli Utenti</h2>
            <p>
                SanityManager permette di risparmiare tempo prenotando direttamente da casa una visita dal medico di base.<br/>
                Ogni utente può visualizzare tutte le visite fatta, avendo accesso anche ai referti di esame e visite specialistiche.
                E' inoltre possibile visualizzare e stampare le proprie ricette direttamenta dal sito web.
            </p>
        </div>
        <div class="col-6 h-25 text-center">
            <h2>Servizi per i Medici di base</h2>
            <p>
                SanityManager permette di compilare visite direttamente online e di prescrivere visite specialistiche,
                esami e ricette in modo semplice e veloce. <br/>
                Ogni Medico è in grado di visualizzare tutti i referti di visite specialistche ed esami per i suoi Pazienti.
            </p>
        </div>
    </div>
    
    <div class="row d-none d-md-block">
        <div class="col-6 h-25 text-center">
            <h2>Servizi per i Medici specialistici</h2>
            <p>
                SanityManager permette di compilare online le visite specialistiche.
                Ogni medico ha accesso ai dati dell' utente e alle passate visite per un'analisi più accurata della
                storia delle malattie del Paziente
            </p>
        </div>
        <div class="col-6 h-25 text-center">
            <h2>Servii per il Servizio Sanitario Provinciale</h2>
            <p>
                SanityManager permette di inserire i dati relativi agli esami svolti dal Paziente. 
                Ogni SSP ha inoltre accesso ad una serie di report territoriali.
            </p>
        </div>
    </div>
    
    <hr/>
    
    <!-- 
        Qua pensavo di scrivere alcune cose aggiuntive che abbiamo fatto per presentarle. Cosa ne pensate ?
        Le scriviamo solo nel docuemnto che creiamo oppure anche qua ? Perchè questa pagina sarebbe da mostrare 
        ai potenziali clienti/utenti.
    
        Vedi per idee sul testo -> https://salute.regione.veneto.it/web/fser/cittadino/app-sanita-km-zero
    -->
    
    <div class="row d-none d-md-block">
        <div class="col-12 h-25 text-center">
            <h1>Informazioni aggiuntive</h1>
        </div>
    </div>
    
    <div class="row d-none d-md-block">
        <div class="col-6 h-25 text-center">
            <p class='numero_elenco'>1.</p>
            <h2>Caricamento asincrono elenchi ricette/visite/esami prescrivibili</h2>
            <p>
                ...
            </p>
        </div>
        <div class="col-6 h-25 text-center">
            <p class='numero_elenco'>2.</p>
            <h2>Gestione prenotazioni</h2>
            <p>
                ...
            </p>
        </div>
    </div>
    
    
    <hr/>
    
    <div class="row d-none d-md-block">
        <div class="col-12 h-25 text-center">
            <h2>Come attivarla</h2>
            <p>
                Per richiedere l'attivazione dell'account personale mandare una mail a ssn@sanitymanager.com
            </p>
        </div>
    </div>
    
</div>


</body>
</html>
