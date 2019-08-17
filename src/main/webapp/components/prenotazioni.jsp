<%@ include file="../global/common.jsp" %>

<%--<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-1 shadow-sm text-white">
    <tr>
        <th scope="col">#</th>
        <th scope="col">First</th>
        <th scope="col">Last</th>
        <th scope="col">Handle</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach begin="1" end="100" var="i">
        <tr>
            <th scope="row"><c:out value="${i}"/></th>
            <td>Mark</td>
            <td>Otto</td>
            <td>@mdo</td>
        </tr>
    </c:forEach>
    </tbody>
</table>--%>
<div class="my-4">
    <p class="font-weight-bold" style="float: left; font-size: 17px">Seleziona la data per la visita:</p>
    <input id="datepicker" class="text-center mb-3" style="float: left" width="276" />
    <button id="loadDate" class="btn btn-gradient btn-block rounded-pill ml-3 mb-3" style="width: 236px; float: left">Visualizza disponibilita'</button>
</div>
<div style="margin-top: 150px">
    <c:if test="${not empty date}">
        <c:forEach begin="1" end="23" step="1" var="i">
            <c:set var="contains" value="false"/>
            <c:if test="${not empty reservations}">
                <c:forEach var="reserv" items="${reservations}">
                    <fmt:parseDate value="${reserv.getTimestamp()}" pattern="yyyy-MM-dd HH:mm" var="timest" />
                    <fmt:formatDate value="${timest}" pattern="HH" var="hour" />
                    <c:if test="${hour eq i}">
                        <c:set var="contains" value="true" />
                        <fmt:formatDate value="${timest}" pattern="yyyy-MM-dd" var="day" />
                        <div class="alert alert-danger text-center my-3" style="width: 500px">
                            <p style="font-style: italic; left: 5px; top: 2px; position: absolute"><c:out value="${day}"></c:out></p>
                            <p style="font-weight: bold; right: 5px; top: 2px; position: absolute"><c:out value="${i}:00"></c:out></p>
                            <p><c:out value="Prenotazione non disponibile"></c:out></p>
                        </div>
                    </c:if>
                </c:forEach>
            </c:if>
            <c:if test="${not contains}">
                <div class="alert alert-dark text-center my-3" style="width: 500px">
                    <p style="font-style: italic; left: 5px; top: 2px; position: absolute"><c:out value="${date}"></c:out></p>
                    <p style="font-weight: bold; right: 5px; top: 2px; position: absolute"><c:out value="${i}:00"></c:out></p>
                    <p><c:out value="Nessuna prenotazione"></c:out></p>
                    <button class="btn btn-gradient btn-block rounded-pill text-center ml-3 mt-2" style="width: 150px">Prenota</button> 
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
        
    }
    
    
</script>
