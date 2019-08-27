<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-3 shadow-sm text-white">
    <tr>
        <th scope="col">Tipo</th>
        <th scope="col">Data Visita</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${esami}" var="esame">
        <tr <c:if test="${!esame.isNew()}" > class='clickable-row' data-href='app/${u_url}/compila_esame?id_paziente=${id_paziente}&id_esame=${esame.getId()}' </c:if>  >
            <th scope="row"><c:out value="${esame.getNome_esame()}"/></th>
            <th scope="row">
                <c:choose>
                    <c:when test="${esame.isNew()}">   <fmt:message key="visita_spec_da_fissare"/>  </c:when>
                    <c:otherwise>  <c:out value="${esame.getTime_esame()}"/>    </c:otherwise>
                </c:choose> 
            </th>
        </tr>
    </c:forEach>
    </tbody>
</table>
