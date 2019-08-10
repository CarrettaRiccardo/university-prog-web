<%-- 
    Document   : Pagina con interfaccia per creare una nuova visita
    Created on : 8 ago 2019, 17:20:32
    Author     : Steve
--%>

<%@ include file="../global/common.jsp" %>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="data" value="${now}" pattern="dd-mm-yyyy" />

<form>
  <div class="form-row">
    <div class="form-group col-md-6">
      <label for="paziente">Paziente</label>
      <input type="email" class="form-control" id="paziente" placeholder="${paz.getNome() + paz.getCognome()}" readonly>
    </div>
    <div class="form-group col-md-6">
      <label for="data">data</label>
      <input type="password" class="form-control" id="data" placeholder="${data}" readonly>
    </div>
  </div>
  <div class="form-group">
    <label for="anamnesi">Anamnesi</label>
    <input type="text" class="form-control" id="anamnesi" placeholder="Il paziente presenta ...">
  </div>
  <button type="submit" class="btn btn-primary">Conferma</button>
</form>