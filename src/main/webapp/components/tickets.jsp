<%@ include file="../global/common.jsp" %>

<fmt:formatNumber value="${10}" type="currency"/>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-6 shadow-sm text-white">
    <tr>
        <th scope="col"><fmt:message key="tipo_prestazione"/></th>
        <th scope="col"><fmt:message key="data"/></th>
        <th scope="col"><fmt:message key="costo"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${tickets}" var="t">
        <tr data-href='app/${u_url}/dettagli_ticket?id_ticket=${t.getId()}'>
            <td>
                <c:choose>
                    <c:when test="${ t.getTipo() eq 'v'.charAt(0) }"><fmt:message key="visita_spec"/></c:when>
                    <c:when test="${ t.getTipo() eq 'e'.charAt(0) }"><fmt:message key="esame"/></c:when>
                </c:choose>
            </td>
            <td><fmt:formatDate value="${t.getTime()}" pattern="dd/MM/yyyy"/></td>
            <td><fmt:formatNumber value="${t.getCosto()}" type="currency"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
