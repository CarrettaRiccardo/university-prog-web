<!--
INIZIO A PROGETTARE UNA STRUTTURA COMUNE (STEVE)
--->

<%@ include file="../../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="time" value="${now}" pattern="yyyy"/>
<jsp:useBean id="monthNames" class="java.text.DateFormatSymbols" />
<c:set value="${monthNames.months}" var="months" />

<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>


<div class="container2">
    <c:forEach items="${grafici}" var="g" varStatus="loop">
       <div class="row my-4">
        <div class="col-12">
            <div id="chart_div_${loop.index}"></div>
        </div>
       </div>
       <br/>
    </c:forEach>       
</div>

<c:forEach items="${grafici}" var="grafici" varStatus="loop">
    <script type="text/javascript">
      <c:if test="${loop.index == 0}">
        google.charts.load('current', {'packages':['corechart']});
      </c:if>
      google.charts.setOnLoadCallback(drawChart${loop.index});      

      function drawChart${loop.index}() {
        var data = google.visualization.arrayToDataTable([
            ['<fmt:message key="stats_lbl_mese"/>' 
                <c:forEach begin="0" end="${grafici.get(loop.index).getArrayDati.get(0).size() - 1}" varStatus="status">
                    ,'${time - status.index}'
                </c:forEach>
            ],
            <c:forEach items="${grafici.get(loop.index).getArrayDati}" var="r" varStatus="status">
                ['${months[ status.getIndex() ]}' 
                    <c:forEach items="${r}" var="v">
                        ,${v}
                    </c:forEach>
                ]
                <c:if test="${!status.last}"> , </c:if>
            </c:forEach>
        ]);

        var options = {
          title: ' <fmt:message key="${grafici.get(loop.index).getTitleGraphic}" /> ',
          hAxis: {title: '<fmt:message key="stats_lbl_mese"/>',  
          titleTextStyle: {color: '#333'}},
          vAxis: {minValue: 0}
        };
        var chart = new google.visualization.AreaChart(document.getElementById('chart_div_${loop.index}'));
        chart.draw(data, options);
      }
    </script>
</c:forEach>
