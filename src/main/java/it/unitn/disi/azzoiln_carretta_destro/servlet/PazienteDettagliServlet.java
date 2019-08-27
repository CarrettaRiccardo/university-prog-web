package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

public class PazienteDettagliServlet extends HttpServlet {
    
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
        request.setAttribute("title", "Dettagli_paziente");
        request.setAttribute("page", "paziente_dettagli");

        // Estraggo la sezione caricata leggendo l'url ed estraendo solo l'ultimo elemento
        String[] parts = request.getRequestURI().split("/");
        String subpage = parts[parts.length - 1].replace("\\?.*", "");
        
        try {
            // ottengo i dati del paziente selezionato
            HttpSession session = request.getSession(false);
            Integer id_paz = Integer.parseInt(request.getParameter("id_paziente"));
            Paziente pz = (Paziente) userDao.getByPrimaryKey(id_paz, "paziente");

            String date = pz.getData_nascita().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);

            session.setAttribute("paz_nome", pz.getNome());
            session.setAttribute("paz_cognome", pz.getCognome());
            session.setAttribute("paz_codice_fiscale", pz.getCf());
            session.setAttribute("paz_data_nascita", date);
            if (pz.getProvincia() != null)
                session.setAttribute("paz_nome_provincia", userDao.Ssp().getNomeProvincia(pz.getProvincia()));
            //if (pz.getId_medico() != null)// giusto per sicurezza
            //    session.setAttribute("paz_medico", (Medico) userDao.getByPrimaryKey(pz.getId_medico(), "medico"));
        } catch (ParseException ex) {
            throw new ServletException("invalid_date_exception");
        } catch (DaoException ex) {
            throw new ServletException("id_paziente_not_valid");
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
