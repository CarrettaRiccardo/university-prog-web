<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-3 shadow-sm text-white">
    <tr>
        <th scope="col">Tipo</th>
        <th scope="col">Data Visita</th>
        <th scope="col">Esito</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${visite}" var="visita">
        <tr>
            <th scope="row"><c:out value="${visita.getNome_visita()}"/></th>
            <th scope="row"><c:out value="${visita.getTime_visita()}"/></th>
            <td><c:out value="${visita.getAnamnesiShort()}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
