<!--
INIZIO A PROGETTARE UNA STRUTTURA COMUNE (STEVE)
--->

<%@ include file="../../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="time" value="${now}" pattern="yyyy"/>
<jsp:useBean id="monthNames" class="java.text.DateFormatSymbols" />      
<%@ page import="it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Statistiche" %>
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
        google.charts.load('current', {'packages':['corechart','bar']});
    </c:if>
                  
    <c:choose>
        <c:when test="${g.getTipoGrafico() eq 0}">            
                google.charts.setOnLoadCallback(drawChart${loop.index});   
                function drawChart${loop.index}() {
                  var data = google.visualization.arrayToDataTable([
                      ['<fmt:message key="stats_lbl_mese"/>' 
                          <c:forEach begin="0" end="${g.getArrayDati().get(0).size() - 1}" varStatus="status">
                              ,'${time - status.index}'
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
        <c:otherwise>${g.getTipoGrafico()} </c:otherwise>
    </c:choose>
    
    </script>
</c:forEach>

        
