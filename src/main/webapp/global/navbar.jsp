<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="utente" scope="session" class="it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente"/>
<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm d-print-none">

    <!-- Logo -->
    <a class="navbar-brand" href="app/home">
        <img src="assets/logo.svg" width="36" height="36" class="d-inline-block align-top mr-2" alt="logo">
        <span class="h4 font-weight-normal d-none d-lg-inline">
            <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
        </span>
    </a>

    <div class="order-lg-12">
        <!-- Button apertura/chiusura menu per smartphone -->
        <button class="btn btn-light ml-2 px-1 py-1 d-lg-none navbar-toggler border-0"
                type="button" data-toggle="collapse" data-target="#navbarMenu"
                aria-controls="navbarMenu" aria-expanded="false" aria-label="Apri menu">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Menu utente con immmagine profilo -->
        <div class="dropdown top-0 right-0 float-left">
            <a class="btn bg-white btn-light dropdown-toggle py-1" href="#" role="button" id="userDropdownMenu"
               data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <c:if test="${ !utente.isSsp() }">
                    <img width="30" height="28" class="rounded-circle d-inline-block align-top mr-1 "
                         onerror="this.onerror=null; this.src='assets/default.jpg'"
                         src="<c:out value="${PHOTOS_DIR}${utente.getFotoSmall()}" />">
                </c:if>
                <span class="d-none d-lg-inline-block">
                    <jsp:getProperty name="utente" property="username"/>
                </span>
            </a>

            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdownMenu">
                <a class="dropdown-item" href="app/${u_url}/settings.handler">Impostazioni</a>
                <a class="dropdown-item" href="app/logout.handler">Logout</a>
            </div>
        </div>

    </div>

    <!-- Menu navigazione -->
    <div class="collapse navbar-collapse float-left" id="navbarMenu">
        <ul class="navbar-nav mr-auto">
            <c:forTokens items="${requestScope.sezioni}" var="sezione" varStatus="s" delims=",">
                <li class="nav-item">
                    <a href="app/${u_url}/${sezione}"
                       class="nav-link font-weight-bold ${(sezione == page ? 'text-gradient-'.concat(s.index + 1) : '') }">
                        <span class="text-capitalize px-2">${sezioni_titles[s.index]}</span>
                    </a>
                </li>
            </c:forTokens>
        </ul>
    </div>

</nav>