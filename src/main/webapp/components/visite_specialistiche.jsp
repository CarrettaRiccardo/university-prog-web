<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date"/>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-3 shadow-sm text-white">
    <tr>
        <th scope="col"><fmt:message key="tipo"/></th>
        <th scope="col"><fmt:message key="data_visita"/></th>
        <th scope="col"><fmt:message key="esito"/></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${visite}" var="visita">
        <tr
                <c:if test="${visita.isDaFissare() and utente.isPaziente()}"> style="font-weight:bold" </c:if>
                <c:if test="${visita.isDaFissare() and not utente.isPaziente()}">style="color: #b1b3b5"</c:if>
                <c:if test="${visita.getTime_visita().getTime() gt now.getTime()}">style="color: #b1b3b5"</c:if>
                <c:if test="${(utente.isPaziente() and (visita.isDaFissare() or visita.getTime_visita().getTime() le now.getTime())) or (not utente.isPaziente() and  visita.getTime_visita().getTime() le now.getTime())}"> data-href='app/${u_url}/compila_visita_spec?id_paziente=${id_paziente}&id_visita=${visita.getId()}' </c:if> <%-- Solo il paziente pu� aprire una visita_spec non fissata (appunto per fissarla) --%>
        >
            <td>
                ${visita.getNome_visita()}</th>
            <td>
                <c:choose>
                    <c:when test="${visita.isDaFissare()}">
                        <fmt:message key="visita_spec_da_fissare"/>
                    </c:when>
                    <c:otherwise><fmt:formatDate value="${visita.getTime_visita()}" pattern="dd/MM/yyyy"/></c:otherwise>
                </c:choose>
            </td>
            <td>${visita.getAnamnesiShort()}</td>
        </tr>
    </c:forEach>

    <%--
        Test di Destro per capire problema caratteri accentati. Commentato da Steve
    
        List<VisitaSpecialistica> v = (List<VisitaSpecialistica>) request.getAttribute("visite");
        char c = v.get(1).getAnamnesiShort().charAt(10);
        System.out.println("char: " + c + ", code: " + ((int) c));
        String s = "Allora mi è sembrato.. ";
        char c2 = s.charAt(10);
        System.out.println("char2: " + c2 + ", code: " + ((int) c2));
        char c3 = '\u00e8';
        System.out.println("char2: " + c3 + ", code: " + ((int) c3));
    
    ${ " - è - ".concat(visita.getAnamnesiShort()) }
    --%>
    </tbody>
</table>
<%--
SAS
${ visite.get(1).getAnamnesiShort() }
ASA
--%>