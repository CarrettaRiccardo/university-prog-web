package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DettagliUtenteServlet extends HttpServlet {

    private UtenteDao userDao;

    @Override
    public void init() throws ServletException {
        DaoFactory daoFactory = (DaoFactory) getServletContext().getAttribute("daoFactory"); //Steve ho tolto super.
        if (daoFactory == null) {
            throw new ServletException("db_error");
        }
        try {
            userDao = daoFactory.getDAO(UtenteDao.class);
        } catch (DaoFactoryException ex) {
            throw new ServletException("db_error");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("title", "Dettagli_utente");
        request.setAttribute("page", "dettagli_utente");

        // Estraggo la sezione caricata leggendo l'url ed estraendo solo l'ultimo elemento
        String[] parts = request.getRequestURI().split("/");
        String subpage = parts[parts.length - 1].replace("\\?.*", "");

        try {
            /*  Matteo
                TODO: controllo permessi acccesso utenti
                TODO: cambiare da session a request (se possibile?)
             */

            // Ottengo i dati dell'utente selezionato in base al tipo
            HttpSession session = request.getSession(false);
            Utente u = null;
            // Caricamento paziente
            if (request.getParameter("id_paziente") != null) {
                Integer id_paz = Integer.parseInt(request.getParameter("id_paziente"));
                u = userDao.getByPrimaryKey(id_paz, "paziente");
            }
            // Caricamento medico
            else if (request.getParameter("id_medico") != null) {
                Integer id_medico = Integer.parseInt(request.getParameter("id_medico"));
                u = userDao.getByPrimaryKey(id_medico, "medico");
            }
            // Caricamento medico specialista
            else if (request.getParameter("id_medico_spec") != null) {
                Integer id_medico = Integer.parseInt(request.getParameter("id_medico_spec"));
                u = userDao.getByPrimaryKey(id_medico, "medico_spec");
            }
            // Errore se il parametro "id_..." non è stato impostato
            else {
                throw new ServletException("id_missing");
            }

            if (u != null) {
                session.setAttribute("dettagli_utente", u);
            } else {
                // Se l'utente non è stao trovato probabilmente non esiste
                throw new ServletException("utente_not_found");
            }
        } catch (DaoException ex) {
            throw new ServletException("id_utente_not_valid");
        }

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
