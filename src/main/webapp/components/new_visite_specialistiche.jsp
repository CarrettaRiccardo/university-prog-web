<%-- 
    Document   : new_visite_specialistiche
    Created on : 16 ago 2019, 16:25:16
    Author     : Steve
--%>

<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate var="data" value="${now}"/>

<form action="app/${u_url}/new_visite_specialistiche" method="POST">
    <div class="form-row">
        <div class="form-group col-md-6">
            <label for="paziente"><fmt:message key="paziente"/></label>
            <input type="email" class="form-control" id="paziente"
                   placeholder="${paziente.getNome()} ${paziente.getCognome()}" readonly>
        </div>
    </div>
    <div class="form-group">
        <label for="autocomplete"><fmt:message key="visita_spec"/></label>
        <select  <c:if test="${empty i_visita}"> id="autocomplete" </c:if> name="id_visita" class="form-control select2-allow-clear" required>
            <c:if test="${not empty i_visita}">
                <option selected value="${i_visita.getId_visita_spec()}"> ${i_visita.getNome_visita()} </option>
            </c:if>
        </select>
        <small class="form-text text-muted"><fmt:message key="suggest_select2"/></small>
    </div>
    <input type="hidden" name="id_paziente" value="${paziente.getId()}">
    <button type="submit" class="btn btn-primary"><fmt:message key="conferma"/></button>
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