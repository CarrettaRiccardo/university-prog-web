<%@ include file="../global/common.jsp" %>

<div class="card bg-light">
    <div class="card-body row">

        <div class="col col-12 col-md-auto text-center">
            <img width="180" height="180" class="rounded-circle shadow mb-2"
                 src="https://images.vexels.com/media/users/3/145908/preview2/52eabf633ca6414e60a7677b0b917d92-male-avatar-maker.jpg">
        </div>

        <div class="col col-12 col-md pl-md-4">
            <h5 class="text-primary">Dati personali</h5>
            <div class="pb-1"> Nome: <b>Giuseppe</b></div>
            <div class="pb-1"> Cognome: <b>Verdi</b></div>
            <div class="pb-1"> Codice fiscale: <b>CJLASJJKFKLFAFS534</b></div>
            <div class="pb-1"> Telefono: <b>331 432 4375</b></div>
        </div>

        <div class="col col-12 col-md">
            <h5 class="text-primary mt-3 mt-md-0">Altro</h5>
            <div class="pb-1"> Altro1: <b>sfdsd fsdggsdg</b></div>
            <div class="pb-1"> Altro2: <b>sdfsdf sdgssdgsdg</b></div>
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
    <a class="btn btn-gradient-${activeIndex + 2} text-white h6 position-absolute" style="top:0; right:0;"
       href="app/<c:out value="${u_url}"/>/new_visita?id_paziente=${param.id_paziente}">
        <span class="font-weight-bolder">+</span>
        <span class="text-capitalize"> <fmt:message key="aggiungi"/> </span>
    </a>
</div>

<div class="card border-top-0" style="border-top-left-radius: 0; border-top-right-radius: 0;">
    <div class="card-body">
        <%-- Inclusione della servlet per caricare i dati --%>
        <jsp:include page="/app/${u_url}/${subpage}"/>
    </div>
</div>

<c:if test="${param.r ne null}">
    <div class="alert alert-success alert-dismissible fade show position-fixed" style="right: 20px; bottom: 0; z-index: 2" role="alert">
        Operazione eseguita con <b>successo</b>!
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>
