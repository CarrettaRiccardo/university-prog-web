<%-- 
    Document   : Pagina con interfaccia per creare una nuova visita
    Created on : 8 ago 2019, 17:20:32
    Author     : Steve
--%>

<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    nuovaVisita
    <thead class="bg-gradient shadow-sm text-white">
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