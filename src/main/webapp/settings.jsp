<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>
    <title>Impostazioni account</title>

    <%@include file="global/head.jsp" %>
</head>

<body>
<jsp:include page="/global/navbar.jsp"/>

<div class="container pt-3">
    <h2 class="mb-4">Impostazioni account</h2>
    <form action="app/settings.handler" method="POST" enctype="multipart/form-data">
        <div class="row">
            <div class="col col-12 col-md-auto text-center">
                <img id="profile_photo" width="256" height="256" class="rounded-circle shadow mb-2"
                     onerror="this.onerror=null; this.src='assets/default.jpg'"
                     src="<c:out value="${foto_profilo}" /> " >
                <input type="file" name="file" accept=".jpg" class="btn btn-block btn-outline-primary mt-3"></input>
            </div>

            <div class="col col-12 col-md">
                <form action="app/settings.handler" method="POST">

                    <h5 class="text-primary">Dati personali</h5>
                    <div class="row">
                        <div class="col col-12 col-lg-6 py-1">
                            <input type="text" class="form-control" disabled value="${nome}" placeholder="Nome"/>
                        </div>
                        <div class="col col-12 col-lg-6 py-1">
                            <input type="text" class="form-control" disabled value="${cognome}" placeholder="Cognome"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col col-12 col-lg-6 py-1">
                            <input type="text" class="form-control" disabled value="${codice_fiscale}" placeholder="Codice fiscale"/>
                        </div>
                        <div class="col col-12 col-lg-6 py-1">
                            <input type="text" class="form-control" disabled value="${data_nascita}" placeholder="Data Nascita"/>
                        </div>
                    </div>

                    <c:if test="${tipo == 'paziente'}">
                        <h5 class="text-primary mt-3">Altro</h5>
                        <div class="row">
                            <div class="col col-12 col-lg-6 py-1">
                                <select class="form-control" name="medico">
                                    <option disabled selected value>-- Seleziona un Medico di Base --</option>
                                    <c:if test="${not empty medici}">
                                        <c:forEach items="${medici}" var="med">
                                            <option value="${med.getId()}" <c:if test="${id_medico == med.getId()}"><c:out value="selected"/></c:if>>
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
                                            <option value="${prov}" <c:if test="${nome_provincia == prov}"><c:out value="selected"/></c:if>>
                                                <c:out value="${prov}"></c:out>
                                            </option>
                                        </c:forEach>
                                    </c:if>
                                  </select>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${tipo == 'medico'}">
                        <h5 class="text-primary mt-3">Altro</h5>
                        <div class="row">
                            <div class="col col-12 col-lg-6 py-1">
                                <input type="text" class="form-control" disabled value="<c:out value="${laurea}"></c:out>" placeholder="Laurea"/>
                            </div>
                            <div class="col col-12 col-lg-6 py-1">
                                <input type="text" class="form-control" disabled value="<c:out value="${carriera}"></c:out>" placeholder="Inizio Carriera"/>
                            </div>
                        </div>
                    </c:if>

                    <button type="submit"
                            <c:if test="${tipo == 'medico'}"><c:out value="disabled"></c:out></c:if>
                            class="btn btn-primary float-right my-3">Salva</button>
            </div>
        </div>
    </form>
</div>
</body>

</html>
