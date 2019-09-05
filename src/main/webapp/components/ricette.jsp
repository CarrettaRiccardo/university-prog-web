<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-5 shadow-sm text-white">
    <tr>
        <th scope="col"><fmt:message key="nome_farmaco"/></th>
        <th scope="col"><fmt:message key="data_presc"/></th>
        <th scope="col"><fmt:message key="quantita"/></th>
        <th scope="col"><fmt:message key="stato"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${ricette}" var="ric">
        <tr <c:if test="${ric.isNew()}"> style=font-weight:bold</c:if>
            <c:if test="${!ric.isNew()}"> data-href='app/${u_url}/dettagli_ricetta?id_paziente=${id_paziente}&id_ricetta=${ric.getId()}' </c:if>
        >
            <td><c:out value="${ric.getNomeFarmaco()}"/></td>
            <td><fmt:formatDate value="${ric.getTime()}" pattern="dd/MM/yyyy"/></td>
            <td><c:out value="${ric.getQuantita()}"/></td>
            <td>
                <c:choose>
                    <c:when test="${ric.isNew()}"> <fmt:message key="ricetta_da_comprare"/> </c:when>
                    <c:otherwise> <fmt:message key="ricetta_comprata"/> </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
