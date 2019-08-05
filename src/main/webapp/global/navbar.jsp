<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-light bg-white shadow-sm">

    <a class="navbar-brand" href="home">
        <img src="/project/assets/logo.svg" width="36" height="36" class="d-inline-block align-top mr-2" alt="logo">
        <span class="h4 font-weight-normal d-none d-sm-inline">
            <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
        </span>
    </a>

    <div class="d-inline-block">
        <div class="dropdown float-left">
            <a class="btn bg-white btn-light dropdown-toggle py-1" href="#" role="button" id="userDropdownMenu"
               data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <img width="30" height="28" class="rounded-circle d-inline-block align-top mr-1 "
                     src="https://images.vexels.com/media/users/3/145908/preview2/52eabf633ca6414e60a7677b0b917d92-male-avatar-maker.jpg">
                <span class="d-none d-sm-inline-block">Giuseppe verdi</span>
            </a>

            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdownMenu">
                <a class="dropdown-item" href="settings.jsp">Impostazioni</a>
                <a class="dropdown-item" href="logout.handler">Logout</a>
            </div>
        </div>

        <c:if test="${param.showMenu}">
            <button class="navbar-toggler btn-light d-md-none float-left ml-2 px-1 py-1 border-0 " type="button"
                    data-toggle="collapse"
                    data-target="#navbarMenu" aria-controls="navbarMenu" aria-expanded="false" aria-label="Apri menu">
                <span class="navbar-toggler-icon"></span>
            </button>
        </c:if>
    </div>

</nav>