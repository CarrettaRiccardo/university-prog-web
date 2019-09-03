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
            <h5 class="text-primary">Dati ricetta</h5>
            <div class="pb-1"> Codice identificativo: <b>#${ricetta.getId()}</b></div>
            <div class="pb-1"> Data prescrizione: <b><fmt:formatDate value="${ricetta.getTime_vendita()}" pattern="dd/MM/yyyy" /></b></div>
            <div class="pb-1"> Ora prescrizione: <b><fmt:formatDate value="${ricetta.getTime_vendita()}" pattern="HH:mm" /></b></div>
            <div class="pb-1"> Farmaco: <b>${ricetta.getNomeFarmaco()}</b></div>
            <div class="pb-1"> Costo: <b>${ricetta.getCosto()}€</b></div>
            <div class="pb-1"> Quantita: <b>${ricetta.getQuantita()}</b></div>
            <div class="pb-1"> Costo totale: <b>${ricetta.getCostoTotale()}€</b></div>
        </div>

        <div class="col col-12 col-lg pl-lg-4 mt-3 mt-lg-0">
            <h5 class="text-primary">Dati generali</h5>
            <div class="pb-1"> Codice medico: <b>#${ricetta.getId_medico()}</b></div>
            <div class="pb-1"> CF paziente: <b>${CF_paziente}</b></div>
        </div>
    </div>
</div>
