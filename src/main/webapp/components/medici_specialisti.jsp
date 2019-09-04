<%--
    Pagina per SSP per vedere i medici della provincia
--%>

<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-2 shadow-sm text-white">
    <tr>
        <th scope="col"></th>
        <th scope="col"><fmt:message key="cognome"/></th>
        <th scope="col"><fmt:message key="nome"/></th>
        <th scope="col"><fmt:message key="specialita"/> </th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${mediciSpecialsti}" var="medico">
        <tr data-href='app/${u_url}/dettagli_utente/stats?id_medico_spec=${medico.getId()}'>
            <th scope="row"><img width="45" height="40" class="rounded-circle"
                                 onerror="this.onerror=null; this.src='assets/default.jpg'"
                                 src="${PHOTOS_DIR}${medico.getFoto()}"/></th>
            <td><c:out value="${medico.getCognome()}"/></td>
            <td><c:out value="${medico.getNome()}"/></td>
            <td><c:out value="${medico.getSpecialita()}"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
