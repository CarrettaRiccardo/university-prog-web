<%@ include file="../global/common.jsp" %>

<fmt:formatNumber value="${10}" type="currency"/>

<script>
    function printClick() {
        window.print();
    }
</script>

<div class="card bg-light">
    <div class="card-body row">

        <!-- QR code -->
        <div class="col col-12 col-lg-auto text-center">
            <img width="225" height="225" class="qr-img rounded-lg shadow mb-2" src="${qrcode}">
            <button class="btn btn-block btn-gradient mx-auto d-print-none" style="max-width: 225px;"
                    onclick="printClick()"><fmt:message key="stampa_ticket"/></button>
        </div>

        <c:choose>
            <c:when test="${ ticket.getTipo() == 'v'.charAt(0) }"><fmt:message key="visita_spec" var="tipo"/></c:when>
            <c:when test="${ ticket.getTipo() == 'e'.charAt(0) }"><fmt:message key="esame" var="tipo"/></c:when>
        </c:choose>

        <div class="col col-12 col-lg pl-lg-4 mt-3 mt-lg-0">
            <h5 class="text-primary"><fmt:message key="dati_ticket"/></h5>
            <div class="pb-1"><fmt:message key="codice_id"/>: <b>#${ticket.getId()}</b></div>
            <div class="pb-1"><fmt:message key="data_pagamento"/>: <b><fmt:formatDate value="${ticket.getTime()}" pattern="dd/MM/yyyy"/></b></div>
            <div class="pb-1"><fmt:message key="costo"/>: <b><fmt:formatNumber value="${ticket.getCosto()}" type="currency"/></b></div>
            <div class="pb-1"><fmt:message key="tipo_prestazione"/>: <b>${tipo}</b></div>
            <div class="pb-1"><fmt:message key="tipologia"/> ${fn:toLowerCase(tipo)}: <b>${dettagli_tipo}</b></div>
        </div>
    </div>
</div>
