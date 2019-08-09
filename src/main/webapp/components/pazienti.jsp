<%--
    Pagina per elencare i pazienti di un determinato medico
--%>

<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-1 shadow-sm text-white">
    <tr>
        <th scope="col"></th>
        <th scope="col">Cognome22</th>
        <th scope="col">Nome</th>
        <th scope="col">Data Nascita</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${pazienti}" var="paz">
            <tr class='clickable-row' data-href='app/dettagli_paziente?id_paziente=${paz.getId()}'> 
                <th scope="row"> <img width="45" height="40" class="rounded-circle" src=" ${contextpath}  ${paz.getFoto()}  "/> </th>
                <td><c:out value="${paz.getCognome()}"/></td>
                <td><c:out value="${paz.getNome()}"/></td>
                <td><c:out value="${paz.getData_nascita()}"/></td>
            </tr>
    </c:forEach>
    </tbody>
</table>
