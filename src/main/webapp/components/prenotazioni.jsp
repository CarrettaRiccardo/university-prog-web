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
    
    <c:if test="${reservations}">
        <c:forEach items="${reservations}" var="reserv"> 
            <div class="alert alert-dark text-center my-3" style="width: 500px">
                <p><c:out value="${reserv.getIdPaziente()}"></c:out></p>
                <p><c:out value="${reserv.getIdMedico()}"></c:out></p>
                <p><c:out value="${reserv.getTimestamp()}"></c:out></p>
            </div>
        </c:forEach>
    </c:if>
    </div>
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
        alert();
    }
    
    
</script>
