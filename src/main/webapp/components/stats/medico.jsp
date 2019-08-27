<%-- 
    Document   : Pagina per mostrare alcune statistiche sui medici
    Created on : 26 ago 2019, 22:45:43
    Author     : Steve
--%>

<%@ include file="../../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="data" value="${now}" pattern="y"/>
<jsp:useBean id="monthNames" class="java.text.DateFormatSymbols" />
<c:set value="${monthNames.months}" var="months" />


<div class="container">
    <div class="row">
        <div class="col-4 text-center">
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
        /*var data = google.visualization.arrayToDataTable([
          ['Mese', '2017', '2016', '2015'],
          ['Gennaio', 0,0,0],
          ['Febbraio', 3,3,4 ],
          ['Marzo', 15,7,15 ],
          ['Aprile', 19,22,7],
          ['Maggio', 23,24,25],
          ['Giugno', 22,22,22 ],
          ['Luglio', 20,15,14 ],
          ['Agosto', 7,9,6],
          ['Settembre', 16,14,13],
          ['Ottobre', 11,13,14 ],
          ['Novembre', 5,7,4 ],
          ['Dicembre', 0,3,7]
        ]);*/
        var data = google.visualization.arrayToDataTable([
            ['Mese', ${now}, ${now}, ${now}],
            <c:forEach items="${ricette}" var="paz" varStatus="status">
                [${months[ status.getIndex() ]} ,1,2,3],
            </c:forEach>
        ]);

        var options = {
          title: 'Fatturato annuale ricambistica',
          hAxis: {title: 'Mese',  titleTextStyle: {color: '#333'}},
          vAxis: {minValue: 0}
        };

        var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
    </script>