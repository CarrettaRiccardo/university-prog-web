<%-- 
    Document   : new_esami
    Created on : 16 ago 2019, 16:25:16
    Author     : Steve
--%>

<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="data" value="${now}"/>

<form action="app/${u_url}/new_esami" method="POST">
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
  <div class="form-group">
    <label for="autocomplete">Esame</label>
    <select id="autocomplete" name="id_esame" class="form-control select2-allow-clear" required></select>
    <small class="form-text text-muted">Click the input text or space to start to digit.</small>
  </div>
    <input type="hidden" name="id_paziente" value="${paziente.getId()}"> 
  <button type="submit" class="btn btn-primary">Conferma</button>
</form>
  
<c:if test="${errore ne null}">
    <div class="alert alert-danger alert-dismissible fade show position-fixed" style="right: 20px; bottom: 0; z-index: 2" role="alert">
        <fmt:message key="error_creation_medico"/>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>