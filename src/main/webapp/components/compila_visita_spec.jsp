<%-- 
    Document   : Pagina utilizzata da MEDICO_SPEC per inserire anamnesi della sua visita e da altre entità per visualizzare riepilogo visita
    Created on : 24 ago 2019, 21:09:27
    Author     : Steve
--%>


<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />


<c:choose>
    <c:when test="${empty i_visita}">  <fmt:formatDate var="data" value="${now}"/>  </c:when>
    <c:when test="${! empty i_visita}"> <fmt:formatDate var="data" value="${i_visita.getTime_visita()}"/> </c:when>
</c:choose>

<form action="app/${u_url}/compila_visita_spec" method="POST">
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
    <textarea class="form-control" id="anamnesi" name="anamnesi" style="height: 150px" <c:if test="${! empty i_visita}">readonly</c:if>><c:choose><c:when test="${empty i_visita}">Il paziente presenta ...</c:when><c:when test="${! empty i_visita}">${i_visita.getAnamnesi()}</c:when></c:choose></textarea>
  </div>
  <c:if test="${sessionScope.utente.getType() == UtenteType.MEDICO_SPEC}">
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