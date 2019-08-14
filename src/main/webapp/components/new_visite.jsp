<%-- 
    Document   : Pagina con interfaccia per creare una nuova visita
    Created on : 8 ago 2019, 17:20:32
    Author     : Steve
--%>

<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="data" value="${now}"/>

<form action="app/${u_url}/new_visita" method="POST">
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
    <label for="anamnesi">Anamnesi</label>
    <input type="hidden" class="form-control" name="id_paziente" value="${paziente.getId()}"> 
    <textarea class="form-control" id="anamnesi" name="anamnesi" style="height: 150px"><c:choose><c:when test="${empty i_anamnesi}">Il paziente presenta ...</c:when><c:when test="${! empty i_anamnesi}">${i_anamnesi}</c:when></c:choose></textarea>
  </div>
  <button type="submit" class="btn btn-primary">Conferma</button>
</form>
  
<c:if test="${errore ne null}">
    <div class="alert alert-danger alert-dismissible fade show position-fixed" style="right: 20px; bottom: 0; z-index: 2" role="alert">
        <strong>Errore</strong> nell' eseguire l'operazione!
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>