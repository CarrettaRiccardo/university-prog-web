<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient shadow-sm text-white">
    <tr>
        <th scope="col">#</th>
        <th scope="col">Cognome</th>
        <th scope="col">Nome</th>
        <th scope="col">Data Nascita</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${pazienti}" var="paz">
        <tr>
            <th scope="row"><c:out value="boh"/></th>
            <td> <img src=" <c:out value="${paz.getFoto()}"/>  "/> </td>
            <td><c:out value="${paz.getId()}"/></td>
            <td><c:out value="${paz.getCognome()}"/></td>
            <td><c:out value="${paz.getNome()}"/></td>
            <td><c:out value="${paz.getData_nascita()}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
