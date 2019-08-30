package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Pagina per ottenere i dati per mostare statistiche a tutte le pagine
 * @author Steve
 */
public class StatisticheServlet extends HttpServlet {

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
    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        String contextPath = getServletContext().getContextPath();
        
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        switch(u.getType()){
            case PAZIENTE: managePaziente(request, u); request.setAttribute("page", "stats/paziente"); break;
            case MEDICO: manageMedico(request, u); request.setAttribute("page", "stats/medico"); break;
            case MEDICO_SPEC: manageMedicoSpec(request, u); request.setAttribute("page", "stats/medico_spec"); break;
            case SSP: manageSsp(request, u); break;
        }
        
        
        //request.setAttribute("id_paziente", request.getParameter("id_paziente"));
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
        rd.include(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    /**
     * Inserisce come param di request stats per :
     * - Andamento numero ricette ultimi 3 anni
     * - Andamento numero visite ultimi 3 anni
     * - Andamento numero esami ultimi 3 anni
     * - Periodi in cui va dal medico durante l'anno per gli ultimi 3 anni
     * @param request
     * @param u 
     */
    private void managePaziente(HttpServletRequest request, Utente u) {
        
    }

    
    /**
     * Inserisce come param di request stats per :
     * - Andamento numero ricette ultimi 3 anni
     * - Andamento numero visite ultimi 3 anni
     * - Andamento numero esami ultimi 3 anni
     * - Numero pazienti
     * - Frequenza prenotazioni
     * @param request
     * @param u 
     */
    private void manageMedico(HttpServletRequest request, Utente u) throws ServletException {
        try{
            ArrayList< ArrayList<Integer> > ricette = userDao.Medico().getStatsRicette(u.getId());
            request.setAttribute("ricette", ricette);
            
            ArrayList< ArrayList<Integer> > visite = userDao.Medico().getStatsVisite(u.getId());
            request.setAttribute("visite", visite);
            
            ArrayList< ArrayList<Integer> > vs = userDao.Medico().getStatsVisiteSpecialistiche(u.getId());
            request.setAttribute("visite_spec", vs);
        }catch(DaoException ex){
            System.out.println(ex.getMessage());
            throw new ServletException(ex.getMessage());
        }
    }

    
    /**
     * Inserisce come param di request stats per :
     * - Andamento numero visite_spec ultimi 3 anni
     * @param request
     * @param u 
     */
    private void manageMedicoSpec(HttpServletRequest request, Utente u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void manageSsp(HttpServletRequest request, Utente u) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
