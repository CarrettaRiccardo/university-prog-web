<%@ include file="../global/common.jsp" %>


<div class="card bg-light">
    <div class="card-body row">

        <div class="col col-12 col-md-auto text-center">
            <img width="180" height="180" class="rounded-circle shadow mb-2"
                 onerror="this.onerror=null; this.src='assets/default.jpg'"
                 src="${PHOTOS_DIR}${paziente.getFoto()}">
        </div>

        <div class="col col-12 col-md pl-md-4">
            <h5 class="text-primary">Dati personali</h5>
            <div class="pb-1"> Nome: <b>${paziente.getNome()}</b></div>
            <div class="pb-1"> Cognome: <b>${paziente.getCognome()}</b></div>
            <div class="pb-1"> Codice fiscale: <b>${paziente.getCf()}</b></div>
            <div class="pb-1"> Data nascita: <b>${paziente.getData_nascita_Stringa()}</b></div>
        </div>

        <div class="col col-12 col-md">
            <h5 class="text-primary mt-3 mt-md-0">Altro</h5>
            <div class="pb-1"> Provincia: <b>${paziente.getProvinciaNome()}</b></div>
        </div>
    </div>
</div>

<div class="mt-3 position-relative">
    <ul class="nav nav-tabs">
        <c:set var="activeIndex" value="${-1}"/>
        <c:forTokens items="${sezioni_dettagli}" var="sezione" varStatus="s" delims=",">
            <li class="nav-item">
                <c:if test="${sezione == subpage}">
                    <c:set var="activeIndex" value="${s.index}"/>
                </c:if>
                <a class="nav-link ${(sezione == subpage ? "active " : "text-gradient-".concat(s.index + 2))}"
                   href="app/${u_url}/dettagli_paziente/${sezione}?id_paziente=${param.id_paziente}">
                    <span class="h6 text-capitalize">${sezioni_dettagli_titles[s.index]}</span>
                </a>
            </li>
        </c:forTokens>
    </ul>
        
    <c:if test="${sessionScope.utente.getType() == UtenteType.MEDICO}">
        <a class="btn btn-gradient-${activeIndex + 2} text-white h6 position-absolute" style="top:0; right:0;"
            href="app/${u_url}/new_${subpage}?id_paziente=${param.id_paziente}"
            <%--<c:choose>
                <c:when test="${sessionScope.utente.getType() == UtenteType.MEDICO}">  href="app/${u_url}/new_${subpage}?id_paziente=${param.id_paziente}" </c:when>
                <c:when test="${sessionScope.utente.getType() == UtenteType.MEDICO_SPEC}">  href="app/${u_url}/compile_visite_specialistiche?id_paziente=${param.id_paziente}" </c:when>
            </c:choose>--%>
            >
            <span class="font-weight-bolder">+</span>
            <span class="text-capitalize"> 
                <fmt:message key="aggiungi"/>
                <%--<c:choose>
                    <c:when test="${sessionScope.utente.getType() == UtenteType.MEDICO}">  <fmt:message key="aggiungi"/> </c:when>
                    <c:otherwise> <fmt:message key="compila_visita_spec"/> </c:otherwise>
                </c:choose>--%>
            </span>
        </a>
    </c:if>
</div>

<div class="card border-top-0" style="border-top-left-radius: 0; border-top-right-radius: 0;">
    <div class="card-body">
        <%-- Inclusione della servlet per caricare i dati --%>
        <jsp:include page="/app/${u_url}/${subpage}"/>
    </div>
</div>

<c:if test="${param.r ne null}">
    <div class="alert alert-success alert-dismissible fade show position-fixed"
         style="right: 20px; bottom: 0; z-index: 2" role="alert">
        Operazione eseguita con <b>successo</b>!
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>
