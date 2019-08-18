<%@ include file="../global/common.jsp" %>

<div class=" text-center my-4">
    <p class="font-weight-bold" style="float: left; font-size: 17px">Seleziona la data per la visita:</p>
    <input id="datepicker" class="text-center mb-3" style="float: left" width="276" />
    <button id="loadDate" class="btn btn-gradient btn-block rounded-pill ml-3 mb-3" style="width: 236px; float: left">Visualizza disponibilita'</button>
    
    <p class="position-absolute" style="right: 20px; top: 20px; position: absolute; font-size: 17px">Medico di base:</p>
    <p class="font-weight-bold position-absolute" style="right: 20px; top: 50px; font-size: 17px">
        <c:out value="${nomeMedico}"></c:out>
    </p>
    <p class="font-italic text-right position-absolute" style="right: 20px; top: 80px; font-size: 13px; width: 170px">(puoi cambiare medico di base nelle impostazioni)</p>
</div>
<div class="text-center" style="margin-top: 150px">
    <c:if test="${not empty date}">
        <c:forEach begin="8" end="18" step="1" var="i">
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
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-danger text-center my-3" style="width: 550px">
                                <p style="font-style: italic; left: 5px; top: 2px; position: absolute"><c:out value="${day}"></c:out></p>
                                <p style="font-weight: bold; right: 5px; top: 2px; position: absolute"><c:out value="${i}:00"></c:out></p>
                                <p><c:out value="Prenotazione non disponibile"></c:out></p>
                            </c:otherwise>
                        </c:choose>
                        </div>
                    </c:if>
                </c:forEach>
            </c:if>
            <c:if test="${not contains}">
                <div class="alert alert-dark text-center my-3" style="width: 550px">
                    <form action="app/paziente/prenotazioni" method="post">
                        <p style="font-style: italic; left: 5px; top: 2px; position: absolute"><c:out value="${date}"></c:out></p>
                        <input type="hidden" name="date" value="<c:out value="${date}"/>"/>
                        <p style="font-weight: bold; right: 5px; top: 2px; position: absolute"><c:out value="${i}:00"></c:out></p>
                        <input type="hidden" name="orario" value="<c:out value="${i}"/>"/>
                        <p><c:out value="Nessuna prenotazione"></c:out></p>
                        <jsp:useBean id="now" class="java.util.Date"/>
                        <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="nowDate"/>
                        <c:if test="${date > nowDate}">
                            <button class="btn btn-gradient btn-block rounded-pill text-center ml-3 mt-2" style="width: 150px">Prenota</button> 
                        </c:if>
                    </form>
                </div>
            </c:if>
        </c:forEach>
    </c:if>
    <c:if test="${empty date}">
        <div class="alert alert-danger text-center my-3" style="width: 250px; height: 50px">
            <p>Seleziona una data!</p>
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
    
    $('#datepicker').datepicker({
        format: 'yyyy-mm-dd'
    });
    
    
    var x = findGetParameter("date");
    if (x != null){ 
        $('#datepicker').val(x);
    }
    
    
</script>
