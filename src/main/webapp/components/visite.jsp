<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-2 shadow-sm text-white">
    <tr>
        <th scope="col">Data Visita</th>
        <th scope="col">Anamnesi</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${visite}" var="visita">
        <tr>
            <th scope="row"><c:out value="${visita.getTime()}"/></th>
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
