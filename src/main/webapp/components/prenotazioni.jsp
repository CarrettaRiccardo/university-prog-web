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
<script>
    $('#datepicker').datepicker({
        format: 'dd/mm/yyyy'
    });
    
    $('#loadDate').click({
        
    });
</script>
