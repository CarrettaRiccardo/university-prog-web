<%@ include file="../global/common.jsp" %>

<div class="text-center my-4">
    <p class="font-weight-bold float-left" style="font-size: 17px">Seleziona la data per la visita:</p>
    <input id="datepicker" class="text-center mb-3 float-left" width="276" />
    <button id="loadDate" class="btn btn-gradient btn-block rounded-pill ml-3 mb-3 float-left" <c:if test="${empty medico || medico.getId() == 0}"><c:out value="disabled"></c:out></c:if> style="width: 236px">Visualizza disponibilita'</button>
    
    <p class="position-absolute" style="right: 20px; top: 20px; font-size: 17px">Medico di base:</p>
    <p class="font-weight-bold position-absolute" style="right: 20px; top: 50px; font-size: 17px">
        <c:if test="${empty medico || medico.getId() == 0}">
            <c:out value="-"></c:out>
        </c:if>
        <c:if test="${medico.getId() > 0}">
            <c:out value="${medico.getNome()} ${medico.getCognome()}"></c:out>
        </c:if>
    </p>
    <p class="font-italic text-right position-absolute" style="right: 20px; top: 80px; font-size: 13px; width: 170px">(puoi cambiare medico di base nelle impostazioni)</p>
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
                                    <div class="alert alert-success text-center my-3" style="width: 550px">
                                        <p style="font-style: italic; left: 5px; top: 2px; position: absolute"><c:out value="${day}"></c:out></p>
                                        <p style="font-weight: bold; right: 5px; top: 2px; position: absolute"><c:out value="${i}:00"></c:out></p>
                                        <p><c:out value="Prenotazione effettuata"></c:out></p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-danger text-center my-3" style="width: 550px">
                                        <p class="font-italic position-absolute" style="left: 5px; top: 2px"><c:out value="${day}"></c:out></p>
                                        <p class="font-weight-bold position-absolute" style="right: 5px; top: 2px;"><c:out value="${i}:00"></c:out></p>
                                        <p><c:out value="Prenotazione non disponibile"></c:out></p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${not contains}">
                    <div class="alert alert-dark text-center my-3" style="width: 550px">
                        <form action="app/paziente/prenotazioni" method="post">
                            <p class="font-italic position-absolute" style="left: 5px; top: 2px"><c:out value="${date}"></c:out></p>
                            <input type="hidden" name="date" value="<c:out value="${date}"/>"/>
                            <p class="font-italic position-absolute" style="right: 5px; top: 2px;"><c:out value="${i}:00"></c:out></p>
                            <input type="hidden" name="orario" value="<c:out value="${i}"/>"/>
                            <p><c:out value="Nessuna prenotazione"></c:out></p>
                            <jsp:useBean id="now" class="java.util.Date"/>
                            <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="nowDate"/>
                            <c:if test="${date > nowDate}">
                                <button class="btn btn-gradient btn-block rounded-pill text-center ml-3 mt-2" style="width: 150px">Prenota visita</button> 
                            </c:if>
                        </form>
                    </div>
                </c:if>
            </c:if>
        </c:forEach>
    </c:if>
    <c:if test="${empty date || empty medico || medico.getId() == 0}">
            <c:choose>
                <c:when test="${empty medico || medico.getId() == 0}">
                    <div class="alert alert-danger text-center my-3" style="width: 250px; height: 70px">
                        <p>Seleziona una medico di base!</p>
                    </div>
                </c:when>
                <c:when test="${empty date}">
                    <div class="alert alert-danger text-center my-3" style="width: 250px; height: 50px">
                        <p>Seleziona una data!</p>
                    </div>
                </c:when>
            </c:choose>
        </div>
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
