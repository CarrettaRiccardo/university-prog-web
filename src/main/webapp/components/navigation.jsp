<%@ page import="it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType" %>
<%@ include file="../global/common.jsp" %>

<c:set var="type" value="${utente.getType()}"/>
<%
    UtenteType type = ((UtenteType) pageContext.getAttribute("type"));
    String categories = "";
    switch (type) {
        case PAZIENTE:
            categories = "prenotazioni,visite,visite_specialistiche,esami,ricette";
            break;
        case MEDICO:
            categories = "pazienti";
            break;
        case MEDICO_SPEC:
            categories = "pazienti";
            break;
        case SSP:
            categories = "medici";
            break;
    }
    String[] categoriesTitle = categories.replaceAll("_", " ").split(",");

    pageContext.setAttribute("categories", categories);
    pageContext.setAttribute("categoriesTitle", categoriesTitle);
%>


<nav id="navbarMenu" class="nav nav-pills d-md-block collapse">
    <c:forTokens items="${categories}" var="cat" varStatus="s" delims=",">
        <a href="app/${cat}"
           class="nav-link w-100 h6 ${(cat == page ? 'text-white bg-gradient-' : 'text-gradient-').concat(s.index + 1) }">
            <span style="text-transform: capitalize">${categoriesTitle[s.index]}</span>
        </a>
    </c:forTokens>
</nav>