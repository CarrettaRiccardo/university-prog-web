<%--
    Pagina per elencare i pazienti di un determinato medico
--%>

<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-1 shadow-sm text-white">
    <tr>
        <th scope="col"></th>
        <th scope="col">Cognome</th>
        <th scope="col">Nome</th>
        <th scope="col">Data Nascita</th>
        <th scope="col">Ultima visita</th>
        <th scope="col">Ultima Ricetta</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${pazienti}" var="paz">
            <tr class='clickable-row' data-href='app/${u_url}/dettagli_paziente/visite?id_paziente=${paz.getId()}'> 
                <th scope="row"> <img width="45" height="40"
                    onerror="this.onerror=null; this.src='assets/default.jpg'" class="rounded-circle"
                    src="${PHOTOS_DIR}${paz.getFoto()}"/> </th>
                <td><c:out value="${paz.getCognome()}"/></td>
                <td><c:out value="${paz.getNome()}"/></td>
                <td><c:out value="${paz.getData_nascita()}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${empty paz.getLastVisita()}">  -  </c:when>
                        <c:when test="${! empty paz.getLastVisita()}"> <c:out value="${paz.getLastVisita()}"/> </c:when>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${empty paz.getLastRicetta()}">  -  </c:when>
                        <c:when test="${! empty paz.getLastRicetta()}"> <c:out value="${paz.getLastRicetta()}"/> </c:when>
                    </c:choose>
                </td>
            </tr>
    </c:forEach>
    </tbody>
</table>
