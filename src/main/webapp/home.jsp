<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>
    <title>Home</title>

    <%@include file="global/head.jsp" %>

    <script type="text/javascript">
        $(document).ready(function () {
            $('#table').DataTable({
                responsive: {
                    details: {
                        display: $.fn.dataTable.Responsive.display.modal({
                            header: function (row) {
                                var data = row.data();
                                return 'Dettagli per ' + data[0] + ' ' + data[1];
                            }
                        }),
                        renderer: $.fn.dataTable.Responsive.renderer.tableAll({
                            tableClass: 'table'
                        })
                    }
                }
            });
        });
    </script>
</head>

<body>

<jsp:include page="global/navbar.jsp">
    <jsp:param name="showMenu" value="true"/>
</jsp:include>

<div class="container-fluid pt-2">
    <div class="row">
        <div class="col-12 col-md-3">
            <nav id="navbarMenu" class="nav nav-pills d-md-block collapse">
                <c:forTokens items="Visite,Visite specialistiche,Esami,Ricette" var="cat" varStatus="s" delims=",">
                    <a class="nav-link w-100 h6 <c:out value="${(s.index == 1 ? 'text-white bg-gradient-' : 'text-gradient-').concat(s.index + 1) }"/>"
                       href="#">
                        <c:out value="${cat}"/>
                    </a>
                </c:forTokens>
            </nav>
        </div>
        <div class="col-12 col-md-9">
            <h2 class="mt-2 mb-4">Visite specialistiche</h2>

            <table id="table" class="table table-striped table-borderless table-hover">
                <thead class="bg-gradient-2 shadow-sm text-white">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">First</th>
                    <th scope="col">Last</th>
                    <th scope="col">Handle</th>
                    <th scope="col">Eafaasf</th>
                    <th scope="col">Adadas</th>
                    <th scope="col">Cfsdfsd</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach begin="1" end="100" var="i">
                    <tr>
                        <th scope="row"><c:out value="${i}"/></th>
                        <td>Mark</td>
                        <td>Otto</td>
                        <td>@mdo</td>
                        <td>Mark</td>
                        <td>Otto</td>
                        <td>@mdo</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <%--
        <c:forTokens items="Visite,Visite specialistiche,Esami,Ricette" var="cat" varStatus="s" delims=",">
            <div class="col-lg-6 p-1">
                <a class="btn btn-block btn-lg btn-gradient text-light bg-gradient-<c:out value="${s.index + 1}"/>">
                    <span class="h5 font-weight-normal"><c:out value="${cat}"/></span>
                </a>
            </div>
        </c:forTokens>
        --%>
    </div>
</div>
</body>

</html>
