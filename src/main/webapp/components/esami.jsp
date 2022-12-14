<%@ include file="../global/common.jsp" %>

<c:if test="${utente.isPaziente()}">
    <div class="mb-5">
        <div class="form-group">
            <label for="autocomplete">Consulta la lista degli esami supportati: </label>
            <select id="autocomplete" class="form-control select2-allow-clear"></select>
            <small class="form-text text-muted">Premi sul campo per iniziare a scrivere.</small>
        </div>
    </div>
</c:if>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-4 shadow-sm text-white">
    <tr>
        <th scope="col"><fmt:message key="tipo"/></th>
        <c:if test="${utente.isSsp()}">
            <td><fmt:message key="paziente"/></td>
        </c:if>
        <td>
            <c:choose>
                <c:when test="${utente.isSsp()}"><fmt:message key="data_nascita"/></c:when>
                <c:otherwise><fmt:message key="data_esame"/></c:otherwise>
            </c:choose>
        </td>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${esami}" var="esame">
        <tr
                <c:if test="${(esame.isDaFissare() and utente.isPaziente()) or (not esame.isNew()) or (utente.isSsp())}"> data-href='app/${u_url}/compila_esame?id_paziente=${esame.getId_paziente()}&id_esame=${esame.getId()}' </c:if>
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
                    <c:otherwise><fmt:formatDate value="${esame.getTime_esame()}" pattern="dd/MM/yyyy"/></c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
