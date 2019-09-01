package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Statistiche;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
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
            case PAZIENTE: managePaziente(request, u);  break;
            case MEDICO: manageMedico(request, u);  break;
            case MEDICO_SPEC: manageMedicoSpec(request, u);  break;
            case SSP: manageSsp(request, u); break;
        }
        
        request.setAttribute("page", "stats");
        request.setAttribute("title", "stats");
        request.setAttribute("user", u);
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
     * - Andamento numero visite_spec ultimi 3 anni
     * - Andamento numero esami ultimi 3 anni
     * - Periodi in cui va dal medico durante l'anno per gli ultimi 3 anni
     * @param request
     * @param u 
     */
    private void managePaziente(HttpServletRequest request, Utente u) throws ServletException {
        try{
            ArrayList< ArrayList<Integer> > ricette = userDao.Paziente().getStatsRicette(u.getId());
            ArrayList< ArrayList<Integer> > visite = userDao.Paziente().getStatsVisite(u.getId());
            ArrayList< ArrayList<Integer> > vs = userDao.Paziente().getStatsVisiteSpecialistiche(u.getId());
            ArrayList< ArrayList<Integer> > esami = userDao.Paziente().getStatsEsami(u.getId());
            ArrayList<Statistiche.LightStats> pren = userDao.Paziente().getStatsPrenotazioni( ((Paziente)u).getId_medico());            
            
            LinkedList<Statistiche> param = new LinkedList<>();
            param.add(new Statistiche<ArrayList<Integer>>(ricette, "stats_ricette",0));
            param.add(new Statistiche<ArrayList<Integer>>(visite, "stats_visite",0));
            param.add(new Statistiche<ArrayList<Integer>>(vs, "stats_visite_spec",0));
            param.add(new Statistiche<ArrayList<Integer>>(esami, "stats_esami",0));
            param.add(new Statistiche<Statistiche.LightStats>(pren, "stats_prenotazioni",2));
            
            request.setAttribute("grafici", param);
        }catch(DaoException ex){
            System.out.println(ex.getMessage());
            throw new ServletException(ex.getMessage());
        }
    }

    
    /**
     * Inserisce come param di request stats per :
     * - Andamento numero ricette ultimi 3 anni
     * - Andamento numero visite ultimi 3 anni
     * - Andamento numero visite_spec ultimi 3 anni
     * - Andamento numero esami ultimi 3 anni
     * - Numero pazienti
     * @param request
     * @param u 
     */
    private void manageMedico(HttpServletRequest request, Utente u) throws ServletException {
        try{
            ArrayList< ArrayList<Integer> > ricette = userDao.Medico().getStatsRicette(u.getId());
            ArrayList< ArrayList<Integer> > visite = userDao.Medico().getStatsVisite(u.getId());
            ArrayList< ArrayList<Integer> > vs = userDao.Medico().getStatsVisiteSpecialistiche(u.getId());
            ArrayList< ArrayList<Integer> > esami = userDao.Medico().getStatsEsami(u.getId());
            
            Integer numero_pazienti = userDao.Medico().getNumPazienti(u.getId());
            
            LinkedList<Statistiche> param = new LinkedList<>();
            param.add(new Statistiche<ArrayList<Integer>>(ricette, "stats_ricette",0));
            param.add(new Statistiche<ArrayList<Integer>>(visite, "stats_visite",0));
            param.add(new Statistiche<ArrayList<Integer>>(vs, "stats_visite_spec",0));
            param.add(new Statistiche<ArrayList<Integer>>(esami, "stats_esami",0));
            param.add(new Statistiche<Integer>( Arrays.asList(numero_pazienti), "stats_num_pazienti",1));
            
            request.setAttribute("grafici", param);
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
    private void manageMedicoSpec(HttpServletRequest request, Utente u) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void manageSsp(HttpServletRequest request, Utente u) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
