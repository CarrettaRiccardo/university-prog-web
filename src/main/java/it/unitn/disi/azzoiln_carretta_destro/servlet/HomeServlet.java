package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class HomeServlet extends HttpServlet {

    private UtenteDao userDao;


    @Override
    public void init() throws ServletException {
        DaoFactory daoFactory = (DaoFactory) getServletContext().getAttribute("daoFactory"); //Steve ho tolto super.

        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UtenteDao.class);
        } catch (DaoFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        String redirectUrl = "";
        switch (u.getType()) {
            case PAZIENTE: redirectUrl = "paziente/prenotazioni"; break;
            case MEDICO: redirectUrl = "medico/pazienti"; break;
            case MEDICO_SPEC: redirectUrl = "medico_spec/pazienti"; break;
            case SSP: redirectUrl = "ssp/medici"; break;
        }
        HttpSession session = request.getSession(false);
        // carico i dati dell'utente in sessione per una navigazione e gestione più semplice
        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                Paziente pz = (Paziente) u;
                session.setAttribute("tipo", "paziente");

                session.setAttribute("utente", pz);
                // per visualizzare il nome del medico in "Prenotazioni"
                if (pz.getId_medico() != null) {
                    Medico myMedico = (Medico) userDao.getByPrimaryKey(pz.getId_medico(), "medico");
                    session.setAttribute("medico", myMedico);
                } else {
                    session.removeAttribute("medico");
                }
            } else {
                if (u.getType() == UtenteType.MEDICO) {
                    Medico m = (Medico) u;
                    session.setAttribute("tipo", "medico");

                    session.setAttribute("utente", m);
                } else if (u.getType() == UtenteType.MEDICO_SPEC) {
                    MedicoSpecialista m = (MedicoSpecialista) u;
                    session.setAttribute("tipo", "medicospec");

                    session.setAttribute("utente", m);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new ServletException("retrieving_doctors_error");
        }

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) contextPath += "/";
        response.sendRedirect(contextPath + "app/" + redirectUrl);
    }

}
