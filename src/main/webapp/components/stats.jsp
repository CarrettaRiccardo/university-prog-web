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

<c:forEach items="${grafici}" var="g" varStatus="loop">
    <script type="text/javascript">
    <c:if test="${loop.index == 0}">
        google.charts.load('current', {'packages':['corechart','bar','calendar']});
    </c:if>
                  
    <c:choose>
        <c:when test="${g.getTipoGrafico() eq 0}">            
                google.charts.setOnLoadCallback(drawChart${loop.index});   
                function drawChart${loop.index}() {
                  var data = google.visualization.arrayToDataTable([
                      ['<fmt:message key="stats_lbl_mese"/>' 
                          <c:forEach begin="0" end="${g.getArrayDati().get(0).size() - 1}" varStatus="status">
                          
                              ,'${time - (g.getArrayDati().get(0).size() - 1 - status.index) }'
                          </c:forEach>
                      ],
                      <c:forEach items="${g.getArrayDati()}" var="r" varStatus="status">
                          ['${months[ status.getIndex() ]}' 
                              <c:forEach items="${r}" var="v">
                                  ,${v}
                              </c:forEach>
                          ]
                          <c:if test="${!status.last}"> , </c:if>
                      </c:forEach>
                  ]);

                  var options = {
                    title: ' <fmt:message key="${g.getTitleGraphic()}" /> ',
                    hAxis: {title: '<fmt:message key="stats_lbl_mese"/>',  
                    titleTextStyle: {color: '#333'}},
                    vAxis: {minValue: 0}
                  };
                  var chart = new google.visualization.AreaChart(document.getElementById('chart_div_${loop.index}'));
                  chart.draw(data, options);
                }
        </c:when>              
        <c:when test="${g.getTipoGrafico() eq 1}">
                google.charts.setOnLoadCallback(drawChart${loop.index});
                function drawChart${loop.index}() {
                      /*var data = google.visualization.arrayToDataTable([
                        ['City', '2010 Population'],
                        ['New York City, NY', 8175000]
                      ]);*/
                      var data = google.visualization.arrayToDataTable([
                      ['' 
                          , '<fmt:message key="${g.getTitleGraphic()}" />'
                      ],
                      <c:forEach items="${g.getArrayDati()}" var="r" varStatus="status">
                          ['',${r}]
                          <c:if test="${!status.last}"> , </c:if>
                      </c:forEach>
                  ]);

                      var options = {
                        title: ' <fmt:message key="${g.getTitleGraphic()}" /> ',
                        chartArea: {width: '80%'},
                        isStacked: true,
                        hAxis: {
                          title: '<fmt:message key="${g.getTitleGraphic()}" />',
                          minValue: 0,
                          viewWindow:{
                            min:0
                          }
                        },
                        vAxis: {
                          title: ''
                        }
                      };
                    var chart = new google.visualization.BarChart(document.getElementById('chart_div_${loop.index}'));
                    chart.draw(data, options);
                }
        </c:when>
        <c:when test="${g.getTipoGrafico() eq 2}">            
                google.charts.setOnLoadCallback(drawChart${loop.index});   
                
                function drawChart${loop.index}() {
                    var dataTable = new google.visualization.DataTable();
                    dataTable.addColumn({ type: 'date', id: 'Date' });
                    dataTable.addColumn({ type: 'number', id: 'Won/Loss' });
                    dataTable.addRows([
                       
                        <c:forEach items="${g.getArrayDati()}" var="r" varStatus="status">
                          [new Date('${r.getData()}'),${r.getCount()}]                          
                          <c:if test="${!status.last}"> , </c:if>
                        </c:forEach>
                     ]);

                    var chart = new google.visualization.Calendar(document.getElementById('chart_div_${loop.index}'));
                    var options = {
                      title: "<fmt:message key="${g.getTitleGraphic()}" />",
                      height: 350,
                      calendar: {
                        dayOfWeekLabel: {
                          fontName: 'Times-Roman',
                          fontSize: 12,
                          color: 'black',
                          bold: true,
                          italic: true,
                        },
                        monthLabel:{
                          fontName: 'Times-Roman',
                          fontSize: 12,
                          color: 'black',
                        },
                        dayOfWeekRightSpace: 10,
                        daysOfWeek: 'DLMMGVS',
                      }
                    };

                    chart.draw(dataTable, options);
                  }
        </c:when> 
    </c:choose>
    
    </script>
</c:forEach>

        
