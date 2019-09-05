<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-4 shadow-sm text-white">
    <tr>
        <th scope="col"><fmt:message key="tipo"/></th>
        <c:if test="${utente.isSsp()}">
            <td><fmt:message key="paziente"/></td>
        </c:if>
        <th scope="col"><fmt:message key="data_visita"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${esami}" var="esame">
        <tr
                <c:if test="${not esame.isDaFissare() or utente.isPaziente()}"> data-href='app/${u_url}/compila_esame?id_paziente=${esame.getId_paziente()}&id_esame=${esame.getId()}' </c:if>
                <c:if test="${esame.isDaFissare() and not utente.isPaziente()}">style="color: #b1b3b5"</c:if>
                <c:if test="${esame.isDaFissare() and utente.isPaziente()}">style="font-weight:bold"</c:if>
        >
            <td><c:out value="${esame.getNome_esame()}"/></td>
            <c:if test="${utente.isSsp()}">
                <td><c:out value="${esame.getNome_paziente()}"/></td>
            </c:if>
            <td>
                <c:choose>
                    <c:when test="${esame.isDaFissare()}"><fmt:message key="visita_spec_da_fissare"/></c:when>
                    <c:otherwise>${esame.getTime_esame()}</c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
