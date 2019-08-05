<%@ include file="global/common.jsp" %> 
<html>
    <head>
        <%@include file="global/head.jsp" %>  <%-- Questo path rimane uguale perchè valutato a compile time--%>
        <title>Choose</title>
        
    </head>
    <body class="bg-light">
        <div class="text-center">
            <h1> <fmt:message key="choose_title"/> </h1>

            <div class="container pt-5">
                <div class="row text-center">
                    <a href="choose.handler?m=m">
                        <div class="card text-white bg-info col6 mx-auto" style="max-width: 18rem;">
                            <img class="card-img-top" src="/project/assets/medico.png" alt="Card image cap">
                            <div class="card-body">
                              <h5 class="card-title">Medico</h5>
                            </div>
                        </div>
                    </a>
                    <a href="choose.handler?m=p">
                        <div class="card text-white bg-warning col6 mx-auto" style="max-width: 18rem;">
                            <img class="card-img-top" src="/project/assets/paziente.png" alt="Card image cap">
                                <div class="card-body">
                                  <h5 class="card-title">Paziente</h5>
                                </div>
                            </div>
                    </a>
                </div>
            </div>
        </div>
    </body>
</html>
