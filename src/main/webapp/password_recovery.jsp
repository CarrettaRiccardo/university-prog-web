<%@ include file="global/common.jsp" %> 
<html>
<head>
    <title>Recupera Password</title>

    <%@include file="global/head.jsp" %>  <%-- Questo path rimane uguale perchè valutato a compile time--%>
    <link rel="stylesheet" href="css/login.css">  <%-- La pagina di login è mappata su /app/login. Quindi mettere solo scc/login dà not found perchè cerca in /app/scss e non trova nulla--%>

</head>
<body>
<form method="POST" action="password_recovery.handler" class="form-login text-center ">
    <img src="assets/logo.svg" width="100" height="100">
    <div class="display-4 mb-4 pb-1 font-weight-normal">
        <span class="text-gradient">Sanity</span><span class="font-weight-light">Manager</span>
    </div>
    <c:if test="${not empty param.success}">
        <div class="alert alert-success" role="alert">    
            <fmt:message key="recovery_success"/> 
        </div>
    </c:if>
    <c:if test="${empty param.success}">
        <c:if test="${not empty param.recovery_error}">
            <div class="alert alert-danger" role="alert">          
                <c:choose>
                    <c:when test="${param.recovery_error == 'invalid_token'}"> <fmt:message key="recovery_error_token"/>  </c:when>
                    <c:when test="${param.recovery_error == 'user'}"> <fmt:message key="recovery_error_username"/>  </c:when>
                    <c:when test="${param.recovery_error == 'sending'}"> <fmt:message key="recovery_error_mail"/>  </c:when>
                    <c:when test="${param.recovery_error == 'existing_token'}"> <fmt:message key="recovery_error_existing"/>  </c:when>
                    <c:otherwise> <fmt:message key="login_error_service"/>  </c:otherwise>
                </c:choose>                  
            </div>
        </c:if>
        <c:if test="${empty param.hasToken}">
            <div class="my-3">Inserisci l'email dell'account da recuperare </div>
            <input type="email" class="form-control mb-3" id="email" name="email" placeholder="Username" required autofocus>
            <button type="submit" class="btn btn-gradient btn-block rounded-pill mb-2">Prosegui</button>
            <a href="password_recovery?hasToken=true" class="btn btn-secondary btn-block text-light rounded-pill mb-2">Ho gi� un token</a>
        </c:if>
        <c:if test="${not empty param.hasToken}">
            <div class="my-3">Inserisci il token ricevuto via email </div>
            <input type="text" class="form-control mb-3" id="token" name="token" placeholder="Token" required autofocus>
            <input type="password" class="form-control mb-3" id="token" minlength="6" name="newpassword" placeholder="Nuova Password" required>
            <button type="submit" class="btn btn-gradient btn-block rounded-pill mb-2">Conferma</button>
        </c:if>
    </c:if>
    
    <a href="login">Torna al login</a>
</form>
</body>
</html>