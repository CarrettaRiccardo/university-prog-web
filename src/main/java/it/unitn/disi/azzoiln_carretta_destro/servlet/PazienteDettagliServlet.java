package it.unitn.disi.azzoiln_carretta_destro.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PazienteDettagliServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("title", "Dettagli paziente");
        request.setAttribute("page", "paziente_dettagli");

        // Estraggo la sezione caricata leggendo l'url ed estraendo solo l'ultimo elemento
        String[] parts = request.getRequestURI().split("/");
        request.setAttribute("subpage", parts[parts.length - 1].replace("\\?.*", ""));

        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
        rd.include(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
