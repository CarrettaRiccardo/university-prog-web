<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>
    <title>Impostazioni account</title>

    <%@include file="global/head.jsp" %>

    <script>
        $("document").ready(function () {
            $("#photo_upload").change(function (e) {
                let file = e.target.files[0];
                if (file) {
                    let reader = new FileReader();
                    reader.onload = function (e) {
                        $('#profile_photo').attr('src', e.target.result);
                    };
                    reader.readAsDataURL(file);
                    $("#file_name").text(file.name);
                }
            });
        });
    </script>
</head>

<body>
<jsp:include page="/global/navbar.jsp"/>

<div class="container pt-3">
    <h2 class="mb-4">Impostazioni account</h2>
    <form action="app/${u_url}/settings.handler" method="POST" enctype="multipart/form-data">
        <div class="row">

            <c:if test="${ !utente.isSsp() }">
                <div class="col col-12 col-md-auto text-center">
                    <img id="profile_photo" width="256" height="256" class="rounded-circle shadow mb-2"
                         onerror="this.onerror=null; this.src='assets/default.jpg'"
                         src="<c:out value="${PHOTOS_DIR}${utente.getFoto()}" /> ">

                    <div class="custom-file d-block">
                        <input id="photo_upload" type="file" name="file" class="custom-file-input" accept=".jpg"/>
                        <label id="file_name" class="custom-file-label text-left" for="photo_upload">Scegli file</label>
                    </div>

                </div>
            </c:if>

            <div class="col col-12 col-md">
                <form action="app/${u_url}/settings.handler" method="POST">

                    <c:if test="${ !utente.isSsp() }">
                    <h5 class="text-primary">Dati personali</h5>
                    </c:if>
                    <c:if test="${ utente.isSsp() }">
                    <h5 class="text-primary">Dati servizio sanitario</h5>
                    </c:if>
                    <div class="row">
                        <div class="col col-12 col-lg-6 py-1">
                            <input type="text" class="form-control" disabled value="${utente.getNome()}"
                                   placeholder="Nome"/>
                        </div>
                        <c:if test="${ !utente.isSsp() }">
                            <div class="col col-12 col-lg-6 py-1">
                                <input type="text" class="form-control" disabled value="${utente.getCognome()}"
                                       placeholder="Cognome"/>
                            </div>
                        </c:if>
                    </div>
                    <c:if test="${ !utente.isSsp() }">
                    <div class="row">
                        <div class="col col-12 col-lg-6 py-1">
                            <input type="text" class="form-control" disabled value="${utente.getCf()}"
                                   placeholder="Codice fiscale"/>
                        </div>
                        <div class="col col-12 col-lg-6 py-1">
                            <input type="text" class="form-control" disabled value="${utente.getData_nascita_Stringa()}"
                                   placeholder="Data Nascita"/>
                        </div>
                    </div>
                    </c:if>

                    <c:if test="${utente.isPaziente()}">
                    <h5 class="text-primary mt-3">Altro</h5>
                    <div class="row">
                        <div class="col col-12 col-lg-6 py-1">
                            <select class="form-control" name="medico">
                                <option disabled selected value>-- Seleziona un Medico di Base --</option>
                                <c:if test="${not empty medici}">
                                    <c:forEach items="${medici}" var="med">
                                        <option value="${med.getId()}" <c:if
                                                test="${utente.getId_medico() == med.getId()}"><c:out value="selected"/></c:if>>
                                            <c:out value="${med.getNome()} ${med.getCognome()} (${med.getLaurea()})"></c:out>
                                        </option>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </div>
                        <div class="col col-12 col-lg-6 py-1">
                            <select class="form-control" name="provincia">
                                <option disabled selected value>-- Seleziona una Provincia --</option>
                                <c:if test="${not empty province}">
                                    <c:forEach items="${province}" var="prov">
                                        <option value="${prov}" <c:if test="${utente.getProvinciaNome() == prov}"><c:out
                                                value="selected"/></c:if>>
                                            <c:out value="${prov}"></c:out>
                                        </option>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </div>
                    </div>
                    </c:if>
                    <c:if test="${utente.isMedico() || utente.isMedicoSpecialista() }">
                    <h5 class="text-primary mt-3">Altro</h5>
                    <div class="row">
                        <div class="col col-12 col-lg-6 py-1">
                            <input type="text" class="form-control" disabled
                                   value="<c:out value="${utente.getLaurea()}"></c:out>" placeholder="Laurea"/>
                        </div>
                        <div class="col col-12 col-lg-6 py-1">
                            <input type="text" class="form-control" disabled
                                   value="<c:out value="${utente.getInizioCarriera()}"></c:out>"
                                   placeholder="Inizio Carriera"/>
                        </div>
                    </div>
                    </c:if>


                    <div>
                        <c:if test="${not empty saved}">
                            <div class="alert alert-success float-top text-center my-3">
                                Modifiche Salvate
                            </div>
                        </c:if>
                        <button type="submit"
                                class="btn btn-primary float-right my-3">Salva
                        </button>
                    </div>
            </div>
        </div>
    </form>
</div>
</body>

</html>
