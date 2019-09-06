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
        <tr data-href='app/${u_url}/new_visite?id_paziente=${id_paziente}&id_visita=${visita.getId()}'>
            <td scope="row"><fmt:formatDate value="${visita.getTime()}" pattern="dd/MM/yyyy"/></td>
            <td>${visita.getAnamnesiShort()}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>