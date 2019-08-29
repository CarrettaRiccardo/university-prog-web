<%-- 
    Document   : Pagina per mostrare alcune statistiche sui medici
    Created on : 26 ago 2019, 22:45:43
    Author     : Steve
--%>

<%@ include file="../../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="time" value="${now}" pattern="y"/>
<jsp:useBean id="monthNames" class="java.text.DateFormatSymbols" />
<c:set value="${monthNames.months}" var="months" />


<div class="container">
    <div class="row">
        <div class="col-6 text-center">
            <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
            <div id="chart_div_ricette"></div>
        </div>
        <div class="col-6 text-center">
            <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
            <div id="chart_div_visite"></div>
        </div>
    </div>
    
    <div class="row">
        <div class="col-6 text-center">
            <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
            <div id="chart_div_visite_spec"></div>
        </div>
        <div class="col-6 text-center">
            <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
            <div id="chart_div"></div>
        </div>
    </div>
    
</div>


<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
            ['Mese',2018,2019 /*${now}, ${now}, ${now}*/],
            <c:forEach items="${ricette}" var="r" varStatus="status">
                ['${months[ status.getIndex() ]}' 
                    <c:forEach items="${r}" var="v">
                        ,${v}
                    </c:forEach>
                ],
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
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
            ['Mese',2018,2019 /*${now}, ${now}, ${now}*/],
            <c:forEach items="${visite}" var="r" varStatus="status">
                ['${months[ status.getIndex() ]}' 
                    <c:forEach items="${r}" var="v">
                        ,${v}
                    </c:forEach>
                ],
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
      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
            ['Mese',2018,2019 /*${now}, ${now}, ${now}*/],
            <c:forEach items="${visite_spec}" var="r" varStatus="status">
                ['${months[ status.getIndex() ]}' 
                    <c:forEach items="${r}" var="v">
                        ,${v}
                    </c:forEach>
                ],
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