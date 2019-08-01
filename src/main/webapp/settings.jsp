<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>
    <title>Impostazioni account</title>

    <%@include file="global/head.jsp" %>
</head>

<body>
<jsp:include page="global/navbar.jsp"/>

<div class="container pt-3">
    <h2 class="mb-4">Impostazioni account</h2>
    <div class="row">

        <div class="col col-12 col-md-auto text-center">
            <img width="256" height="256" class="rounded-circle shadow mb-2"
                 src="https://images.vexels.com/media/users/3/145908/preview2/52eabf633ca6414e60a7677b0b917d92-male-avatar-maker.jpg">
            <button type="submit" class="btn btn-block btn-outline-primary mb-5">Modifica foto</button>
        </div>

        <div class="col col-12 col-md">
            <form>

                <h5 class="text-primary">Dati personali</h5>
                <div class="row">
                    <div class="col col-12 col-lg-6 py-1">
                        <input type="text" class="form-control" placeholder="Nome">
                    </div>
                    <div class="col col-12 col-lg-6 py-1">
                        <input type="text" class="form-control" placeholder="Cognome">
                    </div>
                </div>
                <div class="row">
                    <div class="col col-12 col-lg-6 py-1">
                        <input type="text" class="form-control" placeholder="Codice fiscale">
                    </div>
                    <div class="col col-12 col-lg-6 py-1">
                        <input type="text" class="form-control" placeholder="Telefono">
                    </div>
                </div>

                <h5 class="text-primary mt-3">Altro</h5>
                <div class="row">
                    <div class="col col-12 col-lg-6 py-1">
                        <input type="text" class="form-control" placeholder="boh">
                    </div>
                    <div class="col col-12 col-lg-6 py-1">
                        <input type="text" class="form-control" placeholder="boh2">
                    </div>
                </div>

                <button type="submit" class="btn btn-primary float-right">Salva</button>
            </form>
        </div>
    </div>
</div>
</body>

</html>
