package it.unitn.disi.azzoiln_carretta_destro.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PazienteDettagliServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("title", "Dettagli_paziente");
        request.setAttribute("page", "paziente_dettagli");

        // Estraggo la sezione caricata leggendo l'url ed estraendo solo l'ultimo elemento
        String[] parts = request.getRequestURI().split("/");
        String subpage = parts[parts.length - 1].replace("\\?.*", "");

        // Controllo che la sottosezione a cui l'utente prova ad accedere è valida
        if (((String) request.getAttribute("sezioni_dettagli")).contains(subpage)) {
            request.setAttribute("subpage", subpage);
            
            RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
            rd.include(request, response);
        } else {
            throw new ServletException("error_code_404");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
