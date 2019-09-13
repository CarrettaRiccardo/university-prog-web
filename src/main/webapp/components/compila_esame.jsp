<%-- 
    Document   : Pagina utilizzata da SSP per inserire risultati esame e da tutti gli altri per vedeere il suo esito
    Created on : 24 ago 2019, 21:09:27
    Author     : Steve
--%>


<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<%@ page import="it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ticket" %>


<c:choose>
    <c:when test="${empty i_esame}">  <fmt:formatDate var="data" pattern="dd/MM/yyyy" value="${now}"/>  </c:when>
    <c:when test="${! empty i_esame}"> <fmt:formatDate var="data" pattern="dd/MM/yyyy" value="${i_visita.getTime_esame()}"/> </c:when>
</c:choose>

<form action="app/${u_url}/compila_esame" method="POST" 
      <c:if test="${sessionScope.utente.getType() == UtenteType.SSP and (empty i_esame or not empty errore)}">
          enctype="multipart/form-data"
      </c:if>>
    <div class="form-row">
      <div class="form-group col-md-6">
        <label for="paziente"><fmt:message key="paziente"/></label>
        <input type="email" class="form-control" id="paziente" placeholder="${paziente.getNome()} ${paziente.getCognome()}" readonly>
      </div>
      <div class="form-group col-md-6">
        <label for="data" class="col-md-12"><fmt:message key="data"/></label>
        <c:if test="${not empty id_esame}">
          <c:if test="${not empty data || not utente.isPaziente()}">
              <input type="text" class="form-control" width="150px" id="data" placeholder="${data}" readonly>
          </c:if>
          <c:if test="${empty data && utente.isPaziente()}">
              <div class="col-md-8 float-left">
                  <input id="datepicker" name="datepicker" class="text-center my-2" autocomplete="off"  />
              </div>
              <div class="col-md-4 float-left">
                  <button class="btn btn-gradient btn-block rounded-pill"><fmt:message key="pren_esame"/></button>
              </div>
          </c:if>
        </c:if>
      </div>
    </div>
        
    <div class="form-group">
        <label for="esito"><fmt:message key="esito"/></label>
        <input type="hidden" class="form-control" name="id_paziente" value="${paziente.getId()}"> 
        <input type="hidden" class="form-control" name="id_esame" value="${id_esame}"> 
        <textarea class="form-control" id="esito" name="esito" style="height: 250px"
                  <c:if test="${not empty i_esame and empty errore}">
                      readonly
                  </c:if>><c:choose><c:when test="${empty i_esame}">I risultati per l'esame sono i seguenti :</c:when><c:when test="${not empty i_esame}">${i_esame.getRisultato()}</c:when></c:choose></textarea>
    </div>
  
    <c:if test="${sessionScope.utente.isSsp() and (empty i_esame or not empty errore)}">
        <div class="custom-file d-block my-3" style="width: 250px">
            <input type="hidden" name="isFile" value="true"/>
            <input id="photo_upload" type="file" name="file" class="custom-file-input" accept=".pdf"/>
            <label id="file_name" class="custom-file-label text-left" for="photo_upload"><fmt:message key="add_file"/></label>
        </div>
    </c:if>
  
  
    <!-- TICKET -->
    <c:if test="${not i_esame.isNew() or not empty errore}">  <!-- Non mostro il Ticket se deve solo selezionare la data -->
        <div class="form-group my-2 mx-3">
            <input required type="checkbox" name="ticket" id="ticket" value="si" <c:if test="${not empty i_esame and empty errore}">checked onclick="return false;"</c:if> /><fmt:message key="ticket_di"/>  <!--Se � gi� settato le rendo readonly tramite il return false-->
            <c:choose>
                <c:when test="${empty i_esame or not empty errore}">  <fmt:formatNumber value = "${Ticket.costo_esami}" type = "currency" />  </c:when>
                <c:when test="${not empty importo_ticket}"> <fmt:formatNumber value = "${importo_ticket}" type = "currency" /> </c:when>
            </c:choose>
            <fmt:message key="pagato"/>
        </div>
    </c:if>
  
    
    <c:if test="${sessionScope.utente.isSsp() and (empty i_esame or not empty errore)}"> <!-- Se sono SSP e sto creando un rapporto visita oppure quello di prima ha fatto errore -->
        <hr>
        <button type="submit" class="btn btn-primary float-left"><fmt:message key="conferma"/></button>
    </c:if>
</form>
    
<c:if test="${(sessionScope.utente.isMedico() or sessionScope.utente.isMedicoSpecialista()) and (not empty hasFile)}">
    <form action="app/download" method="POST">
        <input type="hidden" name="filePath" value="${filePath}"/>
        <input type="hidden" name="isDownload" value="true"/>
        <input type="submit" class="btn btn-gradient my-2" style="width: 150px" value="Download file"></input>
    </form>
</c:if>
  
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