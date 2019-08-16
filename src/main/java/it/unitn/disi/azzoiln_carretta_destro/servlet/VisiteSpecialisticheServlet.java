package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica;
import it.unitn.disi.azzoiln_carretta_destro.utility.Common;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class VisiteSpecialisticheServlet extends HttpServlet {
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
        if (request.getRequestURI().indexOf("new_visite_specialistiche") > 0) {  //voglio accedere alla pagina per creare una nuova Visita
            manageNewVisita(request, response);
            return;
        }
        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        List<VisitaSpecialistica> visite;

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                request.setAttribute("title", "Visite_spec_paziente"); //per personalizzare il titolo che viene mostrato
                visite = userDao.getVisiteSpecialistiche(u.getId());
            } else if (u.getType() == UtenteType.MEDICO || u.getType() == UtenteType.MEDICO_SPEC) {
                request.setAttribute("title", "Visite_spec_medico");
                request.setAttribute("nome", ((Medico)u).getNome() + ((Medico)u).getCognome());  //per mostrare il nome del medico loggato
                Integer id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                visite = userDao.getVisiteSpecialistiche(id_paziente);
            }
            else{ //sono SSP, non posso vedere le visite delle persone
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));
                return;
            }

            request.setAttribute("visite", visite);
            request.setAttribute("page", "visite_specialistiche");
            RequestDispatcher rd = request.getRequestDispatcher(request.getRequestURI().contains("dettagli_paziente") ? "/components/visite_specialistiche.jsp" : "/base.jsp");
            rd.include(request, response);
        } catch (IdNotFoundException e) {
            throw new ServletException("paziente_not_found");
        } catch (DaoException e) {
            throw new ServletException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new ServletException("id_paziente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }
    }
    
    
    
    
    /**
     * Come in RicetteServlet.java
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    private void manageNewVisita(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id_paziente = -1;
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        if (request.getParameter("id_paziente") == null)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));

        try {
            id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ServletException("id_paziente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }

        request.setAttribute("title", "crea_visita_specialistica");
        request.setAttribute("page", "new_visite_specialistiche");
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");

        Paziente paz = null;
        try {
            paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
        } catch (DaoException ex) {
            throw new ServletException(ex.getMessage());
        }
        request.setAttribute("paziente", paz);
        request.setAttribute("url_rest",Common.getDomain(request) + getServletContext().getInitParameter("url_visite_spec_rest"));  //per url WB per autocompletamento
        rd.forward(request, response);
    }
    
    
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        
        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        VisitaSpecialistica v = VisitaSpecialistica.loadFromHttpRequest(request, u);
        
        boolean inserito;
        try {
            inserito = userDao.Medico().addVisitaSpecialistica(v);
        } catch (DaoException ex) {
            inserito = false;
        }
        
        if (inserito){
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/dettagli_paziente/visite_specialistiche?id_paziente=" + v.getId_paziente() + "&r"));
            return;
        }
        

        request.setAttribute("i_anamnesi", v.getAnamnesi());
        request.setAttribute("errore", "errore");
        manageNewVisita(request, response);
    }
}
