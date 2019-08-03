<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Login</title>

    <%@include file="global/head.jsp" %>
    <link rel="stylesheet" href="css/login.css">
</head>

<body>
<form method="GET" action="home.jsp" class="form-login text-center ">
    <img src="assets/logo.svg" width="100" height="100">
    <div class="display-4 mb-4 pb-1 font-weight-normal">
        <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
    </div>
    <input type="email" class="form-control" id="username" name="username" placeholder="Username" required autofocus>
    <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
    <div class="custom-control custom-checkbox secondary text-center mb-3">
        <input type="checkbox" class="custom-control-input" id="customCheck1">
        <label class="custom-control-label" for="customCheck1">Ricordami</label>
    </div>
    <button type="submit" class="btn btn-gradient btn-block rounded-pill mb-2">Accedi</button>
    <a href="password_recovery">Password dimenticata?</a>
</form>

</body>

</html>