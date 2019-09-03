<%-- 
    Document   : Pagina utilizzata da MEDICO_SPEC per inserire anamnesi della sua visita e da altre entit� per visualizzare riepilogo visita
    Created on : 24 ago 2019, 21:09:27
    Author     : Steve
--%>


<%@ include file="../global/common.jsp" %>
<%@ page import="it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ticket" %>
<jsp:useBean id="now" class="java.util.Date" />


<c:choose>
    <c:when test="${empty i_visita}">  <fmt:formatDate var="data" value="${now}"/>  </c:when>
    <c:when test="${! empty i_visita}"> <fmt:formatDate var="data" value="${i_visita.getTime_visita()}"/> </c:when>
</c:choose>

<form action="app/${u_url}/compila_visita_spec" method="POST">
  <div class="form-row">
    <div class="form-group col-md-6">
      <label for="paziente">Paziente</label>
      <input type="email" class="form-control" id="paziente" placeholder="${paziente.getNome()} ${paziente.getCognome()}" readonly>
    </div>
    <div class="form-group col-md-6">
    <label for="data" class="col-md-12">Data</label>
      <c:if test="${not empty i_visita}">
        <c:if test="${not empty data || not utente.isPaziente()}">
            <input type="text" class="form-control" width="150px" id="data" placeholder="${data}" readonly>
        </c:if>
        <c:if test="${empty data && utente.isPaziente()}">
            <div class="col-md-8 float-left">
                <input id="datepicker" name="datepicker" class="text-center my-2" autocomplete="off" />
            </div>
            <div class="col-md-4 float-left">
                <button class="btn btn-gradient btn-block rounded-pill">Prenota visita</button>
            </div>
        </c:if>
      </c:if>
    </div>
  </div>
    
  <div class="form-group">
    <label for="anamnesi">Anamnesi</label>
    <input type="hidden" class="form-control" name="id_paziente" value="${paziente.getId()}"> 
    <input type="hidden" class="form-control" name="id_visita" value="${id_visita}"> 
    <textarea class="form-control" id="anamnesi" name="anamnesi" style="height: 150px" 
              <c:if test="${not empty i_visita}">
                  readonly
              </c:if>
                  ><c:choose><c:when test="${empty i_visita}">Il paziente presenta ...</c:when><c:when test="${not empty i_visita}">${i_visita.getAnamnesi()}</c:when></c:choose></textarea>
  </div>
  
  <div class="form-group">
    <label for="cura">Cure da seguire</label>
    <textarea class="form-control" id="cura" name="cura" style="height: 150px" 
              <c:if test="${! empty i_visita}">
                  readonly
              </c:if>
              
        ><c:choose>
            <c:when test="${empty i_visita}"></c:when>
            <c:when test="${! empty i_visita}">${i_visita.getCura()}</c:when>
        </c:choose></textarea>
  </div>
  
  <div class="form-group">
        <input required type="checkbox" name="ticket" id="ticket" value="si" <c:if test="${! empty i_visita}">checked onclick="return false;"</c:if> />Ticket di  <!--Se � gi� settato le rendo readonly tramite il return false-->
        <c:choose>
            <c:when test="${empty i_visita}">  <fmt:formatNumber value = "${Ticket.costo_visite_specialistiche}" type = "currency" />  </c:when>
            <c:when test="${! empty i_visita}"> <fmt:formatNumber value = "${importo_ticket}" type = "currency" /> </c:when>
        </c:choose>
    PAGATO
  </div>
  
  <c:if test="${sessionScope.utente.getType() == UtenteType.MEDICO_SPEC and empty i_visita}">
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
<script>
    var dateToday = new Date();
    $('#datepicker').datepicker({
        format: 'yyyy-mm-dd',
        minDate: dateToday
    });
</script>