<%-- 
    Document   : Pagina con interfaccia per creare una nuova ricetta per un farmaco
    Created on : 8 ago 2019, 17:20:32
    Author     : Steve
--%>

<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="data" value="${now}"/>

<form action="app/new_visita" method="POST">
  <div class="form-row">
    <div class="form-group col-md-6">
      <label for="paziente">Paziente</label>
      <input type="email" class="form-control" id="paziente" placeholder="${paziente.getNome()}  ${paziente.getCognome()}" readonly>
    </div>
    <div class="form-group col-md-6">
      <label for="data">Data</label>
      <input type="password" class="form-control" id="data" placeholder="${data}" readonly>
    </div>
  </div>
  <div class="form-row">
    <div class="form-group col-md-8">
      <label for="anamnesi">Seleziona farmaco</label>
      <!--<input type="search" id="form-autocomplete" class="form-control mdb-autocomplete">
      <input type="hidden" id="form-autocomplete-value" name="id_farmaco">-->
      <select id="autocomplete" name="autocomplete" class="form-control select2-allow-clear"></select>
      <small class="form-text text-muted">Click the input text or space to start to digit.</small>
    </div>
    <div class="form-group col-md-4">
      <label for="quantita">Quantità</label>
      <input type="number" class="form-control" id="quantita" placeholder="1">
    </div>
  </div>
  <input type="hidden" class="form-control" name="id_paziente" value="${paziente.getId()}"> 
  <button type="submit" class="btn btn-primary">Conferma</button>
</form>