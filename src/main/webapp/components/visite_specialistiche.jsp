<%@ page import="it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica" %>
<%@ page import="java.util.List" %>
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
        <tr class='clickable-row'
            data-href='app/${u_url}/compila_visita_spec?id_paziente=${id_paziente}&id_visita=${visita.getId()}'>
            <th scope="row">${visita.getNome_visita()}</th>
            <th scope="row">
                <c:choose>
                    <c:when test="${empty visita.getTime_visita()}">
                        <fmt:message key="visita_spec_da_fissare"/>
                    </c:when>
                    <c:otherwise> ${visita.getTime_visita()} </c:otherwise>
                </c:choose>
            </th>
            <td>${ " - è - ".concat(visita.getAnamnesiShort()) }</td>
        </tr>
    </c:forEach>

    <%
        List<VisitaSpecialistica> v = (List<VisitaSpecialistica>) request.getAttribute("visite");
        char c = v.get(1).getAnamnesiShort().charAt(10);
        System.out.println("char: " + c + ", code: " + ((int) c));
        String s = "Allora mi è sembrato.. ";
        char c2 = s.charAt(10);
        System.out.println("char2: " + c2 + ", code: " + ((int) c2));
        char c3 = '\u00e8';
        System.out.println("char2: " + c3 + ", code: " + ((int) c3));
    %>
    </tbody>
</table>
SAS
${ visite.get(1).getAnamnesiShort() }
ASA