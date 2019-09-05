<%--
    Pagina per elencare i pazienti di un determinato medico
--%>

<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-1 shadow-sm text-white">
    <tr>
        <th scope="col"></th>
        <th scope="col"><fmt:message key="cognome"/></th>
        <th scope="col"><fmt:message key="nome"/></th>
        <th scope="col"><fmt:message key="data_nascita"/></th>
        <th scope="col"><fmt:message key="ultima_visita"/></th>
        <th scope="col"><fmt:message key="ultima_ricetta"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${pazienti}" var="paz">
        <tr data-href='app/${u_url}/dettagli_utente/visite?id_paziente=${paz.getId()}'>
            <td>
                <img width="45" height="40"
                     onerror="this.onerror=null; this.src='assets/default.jpg'" class="rounded-circle"
                     src="${PHOTOS_DIR}${paz.getFoto()}"/>
            </td>
            <td><c:out value="${paz.getCognome()}"/></td>
            <td><c:out value="${paz.getNome()}"/></td>
            <td><fmt:formatDate value="${paz.getData_nascita()}" pattern="dd/MM/yyyy"/></td>
            <td>
                <c:choose>
                    <c:when test="${empty paz.getLastVisita()}"> - </c:when>
                    <c:when test="${! empty paz.getLastVisita()}"><fmt:formatDate value="${paz.getLastVisita()}" pattern="dd/MM/yyyy"/></c:when>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${empty paz.getLastRicetta()}"> - </c:when>
                    <c:when test="${! empty paz.getLastRicetta()}"><fmt:formatDate value="${paz.getLastRicetta()}" pattern="dd/MM/yyyy"/></c:when>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
