<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="nowDate"/>

<div class="row text-left my-4">   
    <div class="col-12">
        <p><fmt:message key="medico_base"/>:
            <strong>
                <c:if test="${empty medico || medico.getId() == 0}">
                    <c:out value="-"></c:out>
                </c:if>
                <c:if test="${medico.getId() > 0}">
                    <c:out value="${medico.getNome()} ${medico.getCognome()}"></c:out>
                </c:if>
            </strong>
        </p>
        <p class="font-italic">(<fmt:message key="cambia_medico"/>)</p>
    </div>
</div>
    
<div class="container text-center my-4">
        <div class="row">
            <p class="font-weight-bold float-left" style="font-size: 17px"><fmt:message key="seleziona_data_visita"/>:</p>
            <input id="datepicker" class="text-center mb-3 float-left" width="276" autocomplete="off"/>
            <button id="loadDate" class="btn btn-gradient btn-block rounded-pill ml-3 mb-3 float-left" <c:if test="${empty medico || medico.getId() == 0}"><c:out value="disabled"></c:out></c:if> style="width: 236px">Visualizza disponibilita'</button>
        </div>
        
        <div class="row">
            <c:if test="${empty date || empty medico || medico.getId() == 0}">
                <c:choose>
                    <c:when test="${empty medico || medico.getId() == 0}">
                        <div class="alert alert-danger text-center my-3" style="width: 250px; height: 70px">
                            <p><fmt:message key="seleziona_medico_base"/></p>
                        </div>
                    </c:when>
                    <c:when test="${empty date}">
                        <div class="alert alert-danger text-center my-3" style="width: 250px; height: 50px">
                            <p><fmt:message key="seleziona_data"/></p>
                        </div>
                    </c:when>
                </c:choose>
            </c:if>
        </div>
</div>
        
<div class="text-center" style="margin-top: 150px">
    <c:if test="${not empty date && not (empty medico || medico.getId() == 0)}">
        <%-- BEGIN E END INDICANO GLI ORARI POSSIBILI, c'e' anche un check sulla servlet --%>
        <c:forEach begin="8" end="17" step="1" var="i">
            <c:if test="${i == 12}">
                <hr class="my-4">
            </c:if>
            <c:if test="${i != 12 && i != 13}">
                <c:set var="contains" value="false"/>
                <c:if test="${not empty reservations}">
                    <c:forEach var="reserv" items="${reservations}">
                        <fmt:parseDate value="${reserv.getTimestamp()}" pattern="yyyy-MM-dd HH:mm" var="timest" />
                        <fmt:formatDate value="${timest}" pattern="HH" var="hour" />
                        <c:if test="${hour eq i}">
                            <c:set var="contains" value="true" />
                            <fmt:formatDate value="${timest}" pattern="yyyy-MM-dd" var="day" />
                            <c:choose>
                                <c:when test="${reserv.getIdPaziente() == sessionScope.utente.getId()}">
                                    <div class="alert alert-success text-center my-3" style="max-width: 550px">
                                        <p style="font-style: italic;"><c:out value="${day}"></c:out></p>
                                        <p style="font-weight: bold;"><c:out value="${i}:00"></c:out></p>
                                        <p><fmt:message key="prenotazione_fatta"/></p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-danger text-center my-3" style="max-width: 550px">
                                        <p class="font-italic position-absolute"><c:out value="${day}"></c:out></p>
                                        <p class="font-weight-bold position-absolute"><c:out value="${i}:00"></c:out></p>
                                        <p><fmt:message key="prenotazione_non_disp"/></p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${not contains}">
                    <form action="app/paziente/prenotazioni" method="post">
                        <div class="row alert alert-dark text-center my-3" style="max-width: 550px">
                                <div class="col-6 text-left">
                                    <p class="font-italic" >${date}</p>
                                    <input type="hidden" name="date" value="${date}"/>
                                </div>
                                <div class="col-6 text-right">
                                    <p class="font-italic" >${i}:00</p>
                                    <input type="hidden" name="orario" value="${i}"/>
                                </div>
                                <div class="col-12 text-left">
                                    <p><fmt:message key="no_prenotazione"/></p>
                                </div>
                                <div class="col-12 text-left">
                                    <c:if test="${date > nowDate}">
                                        <button class="btn btn-gradient btn-block rounded-pill text-center ml-3 mt-2" style="max-width: 150px"><fmt:message key="prenota_visita"/></button> 
                                    </c:if>
                                </div>
                        </div>
                    </form>
                </c:if>
            </c:if>
        </c:forEach>
    </c:if>
</div>
        
        
<script>
    function findGetParameter(parameterName) {
        var result = null,
            tmp = [];
        location.search
            .substr(1)
            .split("&")
            .forEach(function (item) {
              tmp = item.split("=");
              if (tmp[0] === parameterName) result = decodeURIComponent(tmp[1]);
            });
        return result;
    }

    $('#loadDate').click(function(){
        window.location.href = window.location.href.split('?')[0] + '?date=' + $('#datepicker').val(), true;
    });
    
    var dateToday = new Date();
    var yesterday = new Date(dateToday);
    yesterday.setDate(dateToday.getDate() - 1);
    $('#datepicker').datepicker({
        format: 'yyyy-mm-dd',
        minDate: yesterday
    });
    
    var x = findGetParameter("date");
    if (x != null){ 
        $('#datepicker').val(x);
    }
</script>
