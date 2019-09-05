<%@ include file="global/common.jsp" %>
<html>
<head>
    <%@include file="global/head.jsp" %>
    <title>Scelta utente</title>
    <link rel="stylesheet" href="css/login.css">
</head>
<body>
<div class="choose-container text-center">
    <img src="assets/logo.svg" width="100" height="100">
    <div class="display-4 mb-4 pb-1 font-weight-normal">
        <h1>
            <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
        </h1>
    </div>

    <h2 class="mb-4 mt-4"><fmt:message key="choose_title"/></h2>

    <a class="btn btn-gradient-4 text-white mx-auto d-flex align-items-center" style="max-width: 400px;"
       href="app/choose.handler?m=m">
        <img src="assets/ic_medico.svg" alt="Medico" width="64" height="64" style="align-self: center">
        <span class="h4 ml-4">Medico</span>
    </a>

    <a class="mt-4 btn btn-gradient-3 text-white mx-auto d-flex align-items-center" style="max-width: 400px;"
       href="app/choose.handler?m=p">
        <img src="assets/ic_paziente.svg" alt="Paziente" width="64" height="64" style="align-self: center">
        <span class="h4 ml-4">Paziente</span>
    </a>
</div>
</body>
</html>