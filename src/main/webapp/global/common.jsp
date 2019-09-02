<%-- 
    Document   : common
    Created on : 4 ago 2019, 13:59:19
    Author     : Steve
    Goal       : Inserire direttiva comuni a tutte le pagine JSP
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType" %>
<%@ page pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="messages"/>
<fmt:setLocale value="it_IT"/>