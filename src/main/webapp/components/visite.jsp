<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-2 shadow-sm text-white">
    <tr>
        <th scope="col">#</th>
        <th scope="col">Anamnesi</th>
        <th scope="col">Data Visita</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${visite}" var="visita">
        <tr>
            <th scope="row"><c:out value="sa"/></th>
            <td><c:out value="${visita.getId()}"/></td>
            <td><c:out value="${visita.getAnamnesiShort()}"/></td>
            <td><c:out value="${visita.getTime()}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
