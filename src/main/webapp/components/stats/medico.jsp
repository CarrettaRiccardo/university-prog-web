<%-- 
    Document   : Pagina per mostrare alcune statistiche sui medici
    Created on : 26 ago 2019, 22:45:43
    Author     : Steve
--%>

<%@ include file="../../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="time" value="${now}" pattern="yyyy"/>
<jsp:useBean id="monthNames" class="java.text.DateFormatSymbols" />
<c:set value="${monthNames.months}" var="months" />





<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChartRicette);      

      function drawChartRicette() {
        var data = google.visualization.arrayToDataTable([
            ['Mese' 
                <c:forEach begin="0" end="${ricette.get(0).size() - 1}" varStatus="status">
                    ,'${time - status.index}'
                </c:forEach>
            ],
            <c:forEach items="${ricette}" var="r" varStatus="status">
                ['${months[ status.getIndex() ]}' 
                    <c:forEach items="${r}" var="v">
                        ,${v}
                    </c:forEach>
                ]
                <c:if test="${!status.last}"> , </c:if>
            </c:forEach>
        ]);

        var options = {
            title: ' <fmt:message key="stats_ricette" /> ',
          hAxis: {title: 'Mese',  titleTextStyle: {color: '#333'}},
          vAxis: {minValue: 0}
        };
        var chart = new google.visualization.AreaChart(document.getElementById('chart_div_ricette'));
        chart.draw(data, options);
      }
</script>

<script type="text/javascript">
      google.charts.setOnLoadCallback(drawChartVisite);     

      function drawChartVisite() {
        var data = google.visualization.arrayToDataTable([
            ['Mese'
                <c:forEach begin="0" end="${visite.get(0).size() - 1}" varStatus="status">
                    ,'${time - status.index}'
                </c:forEach>
             ],
            <c:forEach items="${visite}" var="r" varStatus="status">
                ['${months[ status.getIndex() ]}' 
                    <c:forEach items="${r}" var="v">
                        ,${v}
                    </c:forEach>
                ]
                <c:if test="${!status.last}"> , </c:if>
            </c:forEach>
        ]);

        var options = {
            title: ' <fmt:message key="stats_visite" /> ',
          hAxis: {title: 'Mese',  titleTextStyle: {color: '#333'}},
          vAxis: {minValue: 0}
        };
        var chart = new google.visualization.AreaChart(document.getElementById('chart_div_visite'));
        chart.draw(data, options);
      }
</script>

<script type="text/javascript">
      google.charts.setOnLoadCallback(drawChartVS);

      function drawChartVS() {
        var data = google.visualization.arrayToDataTable([
            ['Mese'
                <c:forEach begin="0" end="${visite_spec.get(0).size() - 1}" varStatus="status">
                    ,'${time - status.index}'
                </c:forEach>
            ],
            <c:forEach items="${visite_spec}" var="r" varStatus="status">
                ['${months[ status.getIndex() ]}' 
                    <c:forEach items="${r}" var="v">
                        ,${v}
                    </c:forEach>
                ]
                <c:if test="${!status.last}"> , </c:if>
            </c:forEach>
        ]);

        var options = {
            title: ' <fmt:message key="stats_visite_spec" /> ',
          hAxis: {title: 'Mese',  titleTextStyle: {color: '#333'}},
          vAxis: {minValue: 0}
        };
        var chart = new google.visualization.AreaChart(document.getElementById('chart_div_visite_spec'));
        chart.draw(data, options);
      }
</script>