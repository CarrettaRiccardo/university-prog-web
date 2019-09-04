<%@ include file="../global/common.jsp" %>
<%@ page pageEncoding="UTF-8" %>

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
            <button class="btn btn-block btn-gradient mx-auto d-print-none" style="max-width: 225px;" onclick="printClick()">Stampa ricetta</button>
        </div>
        <div class="col col-12 col-lg pl-lg-4 mt-3 mt-lg-0">
            <h5 class="text-primary"><fmt:message key="dati_ricetta"/></h5>
            <div class="pb-1"> <fmt:message key="codice_id"/>: <b>#${ricetta.getId()}</b></div>
            <div class="pb-1"> <fmt:message key="data_presc"/>: <b><fmt:formatDate value="${ricetta.getTime_vendita()}" pattern="dd/MM/yyyy" /></b></div>
            <div class="pb-1"> <fmt:message key="ora_presc"/>: <b><fmt:formatDate value="${ricetta.getTime_vendita()}" pattern="HH:mm" /></b></div>
            <div class="pb-1"> <fmt:message key="farmaco"/>: <b>${ricetta.getNomeFarmaco()}</b></div>
            <div class="pb-1"> <fmt:message key="costo"/>: <b>${ricetta.getCosto()}€</b></div>
            <div class="pb-1"> <fmt:message key="quantita"/>: <b>${ricetta.getQuantita()}</b></div>
            <div class="pb-1"> <fmt:message key="costo_totale"/>: <b>${ricetta.getCostoTotale()}€</b></div>
        </div>

        <div class="col col-12 col-lg pl-lg-4 mt-3 mt-lg-0">
            <h5 class="text-primary"><fmt:message key="dati_generali"/></h5>
            <div class="pb-1"> <fmt:message key="codcice_medico"/>: <b>#${ricetta.getId_medico()}</b></div>
            <div class="pb-1"> <fmt:message key="cf_paziente"/>: <b>${CF_paziente}</b></div>
        </div>
    </div>
</div>
