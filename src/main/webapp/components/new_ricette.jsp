<%-- 
    Document   : Pagina con interfaccia per creare una nuova ricetta per un farmaco
    Created on : 8 ago 2019, 17:20:32
    Author     : Steve
--%>

<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="data" value="${now}"/>

<form action="app/${u_url}/new_ricette" method="POST">
  <div class="form-row">
    <div class="form-group col-md-6">
      <label for="paziente">Paziente</label>
      <input type="text" class="form-control" id="paziente" placeholder="${paziente.getNome()} ${paziente.getCognome()}" readonly>
    </div>
    <div class="form-group col-md-6">
      <label for="data">Data</label>
      <input type="text" class="form-control" id="data" placeholder="${data}" readonly>
    </div>
  </div>
  <div class="form-row">
    <div  <c:choose><c:when test="${! empty i_ricetta}">class="form-group col-md-6"</c:when><c:when test="${empty i_ricetta}">class="form-group col-md-8"</c:when></c:choose>  >
      <label for="anamnesi">Seleziona farmaco</label>
      <!--<input type="search" id="form-autocomplete" class="form-control mdb-autocomplete">
      <input type="hidden" id="form-autocomplete-value" name="id_farmaco">-->
      <select <c:if test="${empty i_ricetta}"> id="autocomplete" </c:if> name="id_farmaco" class="form-control select2-allow-clear" required>
            <c:if test="${empty i_ricetta}">
                <option selected> ${i_ricetta.getNomeFarmaco()} </option>
            </c:if>
      </select>
      <small class="form-text text-muted">Click the input text or space to start to digit.</small>
    </div>
            
    <div class="form-group col-md-4">
      <label for="quantita">Quantità</label>
      <input type="number" class="form-control" id="quantita" name="qta" value="<c:choose><c:when test="${! empty i_ricetta}">${i_ricetta.getQuantita()}</c:when><c:when test="${empty i_ricetta}">1</c:when></c:choose>"
             placeholder="1" required min='1' max='300'>
    </div>
    
    <c:if test="${empty i_ricetta}">
        <div class="form-group col-md-2">
          <label for="ticket">Costo Ticket</label>
          <input type="text" class="form-control" id="ticket" value="${i_ticket.getCosto()}  >
        </div>
    </c:if>
  </div>
  <input type="hidden" name="id_paziente" value="${paziente.getId()}"> 
  <c:if test="${sessionScope.utente.getType() == UtenteType.MEDICO_SPEC and empty i_ricetta}">
    <button type="submit" class="btn btn-primary">Conferma</button>
  </c:if>
</form>
  
<c:if test="${errore ne null}">
    <div class="alert alert-danger alert-dismissible fade show position-fixed" style="right: 20px; bottom: 0; z-index: 2" role="alert">
        <fmt:message key="error_creation_medico"/>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>