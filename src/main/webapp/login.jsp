<%@ include file="global/common.jsp" %>
<html>
<head>
    <title><fmt:message key="login"/></title>

    <%@include file="global/head.jsp" %>
    <%-- Questo path rimane uguale perchè valutato a compile time--%>
    <link rel="stylesheet" href="css/login.css">
    <%-- La pagina di login è mappata su /app/login. Quindi mettere solo scc/login dà not found perchè cerca in /app/scss e non trova nulla--%>

    <script>
        function getCookie(c_name) {
            var c_value = document.cookie,
                c_start = c_value.indexOf(" " + c_name + "=");
            if (c_start == -1) c_start = c_value.indexOf(c_name + "=");
            if (c_start == -1) {
                c_value = null;
            } else {
                c_start = c_value.indexOf("=", c_start) + 1;
                var c_end = c_value.indexOf(";", c_start);
                if (c_end == -1) {
                    c_end = c_value.length;
                }
                c_value = unescape(c_value.substring(c_start, c_end));
            }
            return c_value;
        }

        $(document).ready(function () {
            var tokenCookie = getCookie("user_token");
            if (tokenCookie) {
                window.location.href = "login.handler"
            }
        });
    </script>
</head>
<body>
<form method="GET" action="login.handler" class="form-login text-center ">
    <img src="assets/logo.svg" width="100" height="100">
    <div class="display-4 mb-4 pb-1 font-weight-normal">
        <h1>
            <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
        </h1>
    </div>
    <c:if test="${not empty param.login_error}">
        <div class="alert alert-danger" role="alert">
            <c:choose>
                <c:when test="${param.login_error == 'reset'}"> <fmt:message key="login_error_recovery"/> </c:when>
                <c:when test="${param.login_error == 'pwd'}"> <fmt:message key="login_error_password"/> </c:when>
                <c:when test="${param.login_error == 'user'}"> <fmt:message key="login_error_username"/> </c:when>
                <c:when test="${param.login_error == 'auth'}"> <fmt:message key="login_error_auth"/> </c:when>
                <c:otherwise> <fmt:message key="login_error_service"/> </c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <input type="email" class="form-control" id="username" name="username" placeholder="Username" required autofocus>
    <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
    <div class="custom-control custom-checkbox secondary text-center mb-3">
        <input type="checkbox" class="custom-control-input" id="customCheck1" name="remember_me">
        <label class="custom-control-label" for="customCheck1">Ricordami</label>
    </div>
    <button type="submit" class="btn btn-gradient btn-block rounded-pill mb-2">Accedi</button>
    <a href="password_recovery">Password dimenticata?</a>
</form>
</body>
</html>