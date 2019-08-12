<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-5 shadow-sm text-white">
    <tr>
        <th scope="col">Nome farmaco</th>
        <th scope="col">Data prescrizione</th>
        <th scope="col">Quantità</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${ricette}" var="ric">
        <tr  <c:if test="${empty ric.getTime_vendita()}"> <c:out value='style=font-weight:bold'/></c:if>    >
            <td><c:out value="${ric.getNomeFarmaco()}"/></td>
            <td><c:out value="${ric.getTime()}"/></td>
            <td><c:out value="${ric.getQuantita()}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
