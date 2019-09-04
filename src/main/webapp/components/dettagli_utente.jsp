<%@ include file="../global/common.jsp" %>


<div class="card bg-light">
    <div class="card-body row">

        <div class="col col-12 col-md-auto text-center">
            <img width="180" height="180" class="rounded-circle shadow mb-2"
                 onerror="this.onerror=null; this.src='assets/default.jpg'"
                 src="${PHOTOS_DIR}${dettagli_utente.getFoto()}">
        </div>

        <div class="col col-12 col-md pl-md-4">
            <h5 class="text-primary"><fmt:message key="dati_personali"/></h5>
            <div class="pb-1"> <fmt:message key="nome"/>: <b>${dettagli_utente.getNome()}</b></div>
            <div class="pb-1"> <fmt:message key="cognome"/> <b>${dettagli_utente.getCognome()}</b></div>
            <div class="pb-1"> <fmt:message key="cf"/>: <b>${dettagli_utente.getCf()}</b></div>
            <div class="pb-1"> <fmt:message key="data_nascita"/>: <b>${dettagli_utente.getData_nascita_Stringa()}</b></div>
        </div>

        <div class="col col-12 col-md">
            <h5 class="text-primary mt-3 mt-md-0"><fmt:message key="altro"/></h5>
            <div class="pb-1"> <fmt:message key="provincia"/> <b>${dettagli_utente.getProvinciaNome()}</b></div>
            <div class="pb-1"> <fmt:message key="comune"/> <b>${dettagli_utente.getComuneNome()}</b></div>
            <div class="pb-1"> <fmt:message key="is_medico"/>: <b>${dettagli_utente.isMedico()}</b></div>
            <div class="pb-1"> <fmt:message key="is_medico_spec"/>: <b>${dettagli_utente.isMedicoSpecialista()}</b></div>
            <c:if test="${ dettagli_utente.isMedico() || dettagli_utente.isMedicoSpecialista() }">
            <div class="pb-1"> <fmt:message key="laurea"/> <b> <c:out value="${dettagli_utente.getLaurea()}" default="-"/></b></div>
            <div class="pb-1"> <fmt:message key="inizio_carriera"/>: <b> <c:out value="${dettagli_utente.getInizioCarriera()}"
                                                           default="-"/></b></div>
        </div>
        </c:if>
        <c:if test="${ dettagli_utente.isMedicoSpecialista() }">
        <div class="pb-1"> <fmt:message key="specialita"/>: <b> <c:out value="${dettagli_utente.getSpecialita()}" default="-"/></b>
            </c:if>
        </div>
    </div>
</div>

<div class="mt-3 row">
    <c:set var="showAdd" value="${sessionScope.utente.getType() == UtenteType.MEDICO}"/>

    <div class="col-12 order-2 order-md-1 <c:if test="${showAdd}">col-md-9 pr-0</c:if> ">
        <ul class="nav nav-tabs">
            <c:forTokens items="${sezioni_dettagli}" var="sezione" varStatus="s" delims=",">
                <li class="nav-item">
                    <c:if test="${sezione == subpage}">
                        <c:set var="activeIndex" value="${s.index}"/>
                    </c:if>
                    <!-- Creazione url subpage -->
                    <c:url var="subpageUrl" value="app/${u_url}/dettagli_utente/${sezione}">
                        <c:choose>
                            <c:when test="${ not empty param.id_paziente }">
                                <c:param name="id_paziente" value="${param.id_paziente}"/>
                            </c:when>
                            <c:when test="${ not empty param.id_medico }">
                                <c:param name="id_medico" value="${param.id_medico}"/>
                            </c:when>
                            <c:when test="${ not empty param.id_medico_spec }">
                                <c:param name="id_medico_spec" value="${param.id_medico_spec}"/>
                            </c:when>
                        </c:choose>
                    </c:url>

                    <a class="nav-link ${(sezione == subpage ? "active " : "text-gradient-".concat(s.index + 2))}"
                       href="${ subpageUrl }">
                        <span class="h6 text-capitalize">${sezioni_dettagli_titles[s.index]}</span>
                    </a>
                </li>
            </c:forTokens>
        </ul>
    </div>

    <c:if test="${showAdd}">
        <div class="col-12 col-md-3 order-1 order-md-2 pl-0">
            <a class="btn btn-gradient-${activeIndex + 2} text-white h6 btn-block mb-0"
               href="app/${u_url}/new_${subpage}?id_paziente=${param.id_paziente}"
                <%--<c:choose>
                    <c:when test="${sessionScope.utente.getType() == UtenteType.MEDICO}">  href="app/${u_url}/new_${subpage}?id_paziente=${param.id_paziente}" </c:when>
                    <c:when test="${sessionScope.utente.getType() == UtenteType.MEDICO_SPEC}">  href="app/${u_url}/compile_visite_specialistiche?id_paziente=${param.id_paziente}" </c:when>
                </c:choose>--%>>
                <span class="font-weight-bolder">+</span>
                <span class="text-capitalize">
                <fmt:message key="aggiungi"/>
                <%--<c:choose>
                    <c:when test="${sessionScope.utente.getType() == UtenteType.MEDICO}">  <fmt:message key="aggiungi"/> </c:when>
                    <c:otherwise> <fmt:message key="compila_visita_spec"/> </c:otherwise>
                </c:choose>--%>
            </span>
            </a>
        </div>
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
        <fmt:message key="op_successo"/>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>
