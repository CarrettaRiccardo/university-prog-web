<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-2 shadow-sm text-white">
    <tr>
        <th scope="col"><fmt:message key="data_visita"/></th>
        <th scope="col"><fmt:message key="anamnesi"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${visite}" var="visita">
        <tr class='clickable-row'
            data-href='app/${u_url}/new_visite?id_paziente=${id_paziente}&id_visita=${visita.getId()}'>
            <td scope="row"><c:out value="${visita.getTime()}"/></td>
            <td><c:out value="${visita.getAnamnesiShort()}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>


<c:if test="${utente.getType() == MEDICO}">
    <div class="fixed-bottom">
        <button>Ciao</button>
    </div>
</c:if>
