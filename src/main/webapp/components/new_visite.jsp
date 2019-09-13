<%-- 
    Document   : Pagina con interfaccia per creare una nuova visita. Vista usata anche da riepilogo visita con modalit�
                 di accesso MEDICO_SPEC
    Created on : 8 ago 2019, 17:20:32
    Author     : Steve
--%>

<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date"/>
<c:choose>
    <c:when test="${empty i_visita}"><fmt:formatDate var="data" value="${now}" pattern="dd/MM/yyyy"/></c:when>
    <c:when test="${not empty i_visita}"><fmt:formatDate var="data" value="${i_visita.getTime()}" pattern="dd/MM/yyyy"/></c:when>
</c:choose>


<form action="app/${u_url}/new_visite" method="POST">
    <div class="form-row">
        <div class="form-group col-md-6">
            <label for="paziente"><fmt:message key="paziente"/></label>
            <input type="email" class="form-control" id="paziente"
                   placeholder="${paziente.getNome()} ${paziente.getCognome()}" readonly>
        </div>
        <div class="form-group col-md-6">
            <label for="data"><fmt:message key="data"/></label>
            <input type="password" class="form-control" id="data" placeholder="${data}" readonly>
        </div>
    </div>
    <div class="form-group">
        <label for="anamnesi"><fmt:message key="anamnesi"/></label>
        <input type="hidden" class="form-control" name="id_paziente" value="${paziente.getId()}">
        <textarea class="form-control" id="anamnesi" name="anamnesi" style="height: 250px"
                  <c:if test="${not empty i_visita and empty errore}">readonly</c:if>><c:choose><c:when
                test="${empty i_visita}">Il paziente presenta ...</c:when><c:when
                test="${! empty i_visita}">${i_visita.getAnamnesi()}</c:when></c:choose></textarea>
    </div>
    <c:if test="${sessionScope.utente.isMedico() and (empty i_visita or not empty errore)}">
        <button type="submit" class="btn btn-primary"><fmt:message key="conferma"/></button>
    </c:if>
</form>

<c:if test="${errore ne null}">
    <div class="alert alert-danger alert-dismissible fade show position-fixed"
         style="right: 20px; bottom: 0; z-index: 2" role="alert">
        <fmt:message key="error_creation_medico"/>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>