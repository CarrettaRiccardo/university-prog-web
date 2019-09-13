<%-- 
    Document   : Pagina utilizzata da MEDICO_SPEC per inserire anamnesi della sua visita e da altre entit� per visualizzare riepilogo visita
    Created on : 24 ago 2019, 21:09:27
    Author     : Steve
--%>


<%@ include file="../global/common.jsp" %>
<%@ page import="it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ticket" %>
<jsp:useBean id="now" class="java.util.Date" />

<c:choose>
    <c:when test="${empty i_visita}">  <fmt:formatDate var="data" value="${now}" pattern="dd/MM/yyyy"/></c:when>
    <c:when test="${! empty i_visita}"> <fmt:formatDate var="data" value="${i_visita.getTime_visita()}" pattern="dd/MM/yyyy"/></c:when>
</c:choose>

<form action="app/${u_url}/compila_visita_spec" method="POST">
  <div class="form-row">
    <div class="form-group col-md-6">
      <label for="paziente"><fmt:message key="paziente"/></label>
      <input type="email" class="form-control" id="paziente" placeholder="${paziente.getNome()} ${paziente.getCognome()}" readonly>
    </div>
    <div class="form-group col-md-6">
    <label for="data" class="col-md-12"><fmt:message key="data"/></label>
      <c:if test="${not empty i_visita}">
        <c:if test="${not empty data || not utente.isPaziente()}">
            <input type="text" class="form-control" width="150px" id="data" placeholder="${data}" readonly>
        </c:if>
        <c:if test="${empty data && utente.isPaziente()}">
            <div class="col-md-8 float-left">
                <input id="datepicker" name="datepicker" class="text-center my-2" autocomplete="off" />
            </div>
            <div class="col-md-4 float-left">
                <button class="btn btn-gradient btn-block rounded-pill"><fmt:message key="pren_visita"/></button>
            </div>
        </c:if>
      </c:if>
    </div>
  </div>
    
  <div class="form-group">
    <label for="anamnesi"><fmt:message key="anamnesi"/></label>
    <input type="hidden" class="form-control" name="id_paziente" value="${paziente.getId()}"> 
    <input type="hidden" class="form-control" name="id_visita" value="${id_visita}"> 
    <textarea class="form-control" id="anamnesi" name="anamnesi" style="height: 250px"
              <c:if test="${not empty i_visita and empty errore}">
                  readonly
              </c:if>
                  ><c:choose><c:when test="${empty i_visita}">Il paziente presenta ...</c:when><c:when test="${not empty i_visita}">${i_visita.getAnamnesi()}</c:when></c:choose></textarea> <!-- Identazione cos� brutta per evitare brutti spazi dentro la TextArea -->
  </div>
  
  <div class="form-group">
    <label for="cura"><fmt:message key="cura"/></label>
    <textarea class="form-control" id="cura" name="cura" style="height: 100px"
              <c:if test="${not empty i_visita and empty errore}">
                  readonly
              </c:if>   
        ><c:choose><c:when test="${empty i_visita}"></textarea></c:when><c:when test="${not empty i_visita}">${i_visita.getCura()}</textarea></c:when></c:choose>
  </div>

<!-- TICKET -->
                
<c:if test="${not i_visita.isDaFissare() or not empty errore}">  <!-- Mostro nel caso stia compilando la visita o c'� stato un errore nel doPost precedente -->
    <div class="form-group">
          <input required type="checkbox" name="ticket" id="ticket" value="si" <c:if test="${not empty i_visita and empty errore}">checked onclick="return false;"</c:if> /><fmt:message key="ticket_di"/>  <!--Se � gi� settato le rendo readonly tramite il return false-->
          <c:choose>
              <c:when test="${empty i_visita or not empty errore}">  <fmt:formatNumber value = "${Ticket.costo_visite_specialistiche}" type = "currency" />  </c:when>
              <c:when test="${not empty i_visita}"> <fmt:formatNumber value = "${importo_ticket}" type = "currency" /> </c:when>
          </c:choose>
      <fmt:message key="pagato"/>
    </div>
</c:if>
  
  <c:if test="${sessionScope.utente.isMedicoSpecialista() and (empty i_visita or not empty errore)}">
    <button type="submit" class="btn btn-primary"><fmt:message key="conferma"/></button>
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
    var yesterday = new Date(dateToday);
    yesterday.setDate(dateToday.getDate() - 1);
    $('#datepicker').datepicker({
        format: 'yyyy-mm-dd',
        minDate: yesterday
    });
</script>