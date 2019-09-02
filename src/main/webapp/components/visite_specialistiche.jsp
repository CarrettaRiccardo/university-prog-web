<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />

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
        <tr class='clickable-row' data-href='app/${u_url}/compila_visita_spec?id_paziente=${id_paziente}&id_visita=${visita.getId()}'  >
            <th scope="row"><c:out value="${visita.getNome_visita()}"/></th>
            <th scope="row">
                <c:choose>
                    <c:when test="${empty visita.getTime_visita()}">   <fmt:message key="visita_spec_da_fissare"/>  </c:when>
                    <c:otherwise>  <c:out value="${visita.getTime_visita()}"/>    </c:otherwise>
                </c:choose>                
            </th>
            <td><c:out value="${visita.getAnamnesiShort()}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
