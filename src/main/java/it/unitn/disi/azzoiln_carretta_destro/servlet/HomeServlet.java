package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utente utente = (Utente) request.getSession(false).getAttribute("utente");
        String redirectUrl ="";
        switch(utente.getType()){
            case PAZIENTE: redirectUrl = "paziente/prenotazioni";break;
            case MEDICO: redirectUrl = "medico/pazienti"; break;
            case MEDICO_SPEC: redirectUrl = "medico_spec/pazienti"; break;
            case SSP: redirectUrl = "ssp/medici"; break;
        }

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) contextPath += "/";
        response.sendRedirect(contextPath + "app/"+ redirectUrl);
    }

}
