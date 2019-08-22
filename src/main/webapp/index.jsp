<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>SanityManager</title>

    <%@include file="global/head.jsp" %>
    <link rel="stylesheet" href="css/index.css">

    <script>
        $(document).ready(function () {
            $('#btnLogin').click(function () {
                window.location.href = "login"
            });
        });
    </script>
</head>

<body class="bg">

<nav class="navbar">
    <a class="navbar-brand ">
        <img src="assets/logo.svg" width="36" height="36" class="d-inline-block align-top mr-2" alt="logo">
        <span class="h4 font-weight-normal d-none d-sm-inline">
            <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
        </span>
    </a>
    <button id="btnLogin" class="btn btn-light rounded-pill font-weight-bold px-5 shadow">Accedi</button>
</nav>

<div class="container-fluid mt-5">
    <div class="row text-center pt-md-5">
        <div class="col-12 col-md-5 pt-md-5">
            <img src="assets/logo.svg" width="128" height="128"/>
            <div class="display-4 pb-2 font-weight-normal">
                <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
            </div>
            <p class="h3 font-weight-lighter text-muted">La rivoluzione nel campo della sanità</p>
        </div>
        <div class="col-12 col-md-7 pl-md-5 pt-5">
            <div class="text-black m-auto" style="max-width: 500px">
            <div class="display-4 font-weight-bold mb-4">Titolo qualcosa</div>
            <div>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et
                dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip
                ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu
                fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia
                deserunt mollit anim id est laborum.
            </div>
            </div>
        </div>
    </div>

    <div class="row d-none d-md-block">
        <div class="col-12 h-25"></div>
    </div>
</div>
    

</body>
</html>
