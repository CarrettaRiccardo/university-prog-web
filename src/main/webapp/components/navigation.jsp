<%@ include file="../global/common.jsp" %>

<nav id="navbarMenu" class="nav nav-pills d-md-block collapse">
    <c:forTokens items="${requestScope.sezioni}" var="sezione" varStatus="s" delims=",">
        <a href="app/${sezione}"
           class="nav-link w-100 h6 ${(sezione == page ? 'text-white bg-gradient-' : 'text-gradient-').concat(s.index + 1) }">
            <span class="text-capitalize">${sezioni_titles[s.index]}</span>
        </a>
    </c:forTokens>
</nav>