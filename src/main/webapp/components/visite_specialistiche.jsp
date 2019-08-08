<%@ include file="../global/common.jsp" %>

<table id="table" class="table table-striped table-borderless table-hover">
    <thead class="bg-gradient-3 shadow-sm text-white">
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
</table>
