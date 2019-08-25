package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Persona;
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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        if (request.getRequestURI().indexOf("compila_visita_spec") > 0) {  //voglio accedere alla pagina per creare una nuova Visita
            manageCompilaVisita(request, response, u);
            return;
        }
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
                request.setAttribute("nome", ((Persona)u).getNome() + ((Persona)u).getCognome());  //per mostrare il nome del medico loggato
                Integer id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                visite = userDao.getVisiteSpecialistiche(id_paziente);
            }
            else{ //sono SSP, non posso vedere le visite delle persone
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));
                return;
            }

            request.setAttribute("visite", visite);
            request.setAttribute("page", "visite_specialistiche");
            request.setAttribute("id_paziente", request.getParameter("id_paziente") );
            RequestDispatcher rd = request.getRequestDispatcher(request.getRequestURI().contains("dettagli_paziente") ? "/components/visite_specialistiche.jsp" : "/base.jsp");
            rd.include(request, response);
        } catch (IdNotFoundException e) {
            throw new ServletException("paziente_not_found");
        } catch (DaoException e) {
            throw new ServletException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new ServletException("id_paziente_not_valid");
        } catch (Exception e) {
            System.out.println(e.getMessage() +  "\n\n\n");
            //throw new ServletException();
            throw new ServletException(e.getMessage());
        }
    }
    
    
    
    
    private void manageCompilaVisita(HttpServletRequest request, HttpServletResponse response, Utente u) throws ServletException, IOException {
        int id_paziente = -1, id_visita = -1;
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        if (request.getParameter("id_paziente") == null || request.getParameter("id_visita") == null)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));

        try {
            id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException();
            id_visita = Integer.parseInt(request.getParameter("id_visita"));
            if (id_visita <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ServletException("id_paziente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }
        
        VisitaSpecialistica vs = null;
        try {
            vs = userDao.getVisitaSpecialistica(id_paziente, id_visita);
        } catch (DaoException ex) {
            System.out.println(ex.getMessage());
            throw new ServletException(ex.getMessage());
        }
        finally{
            if(vs == null) throw new ServletException("visita_spec_not_found");
        }
        
        if( !vs.isNew() || u.getType() != UtenteType.MEDICO_SPEC){ //mostro i campi in readonly, altrimenti compilabili
            request.setAttribute("i_visita", vs);
            request.setAttribute("title", "view_visita_specialistica");
        }
        else{
            request.setAttribute("title", "compila_visita_specialistica");
        }

        request.setAttribute("page", "compila_visita_spec");
        request.setAttribute("id_visita", request.getParameter("id_visita"));
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");

        Paziente paz = null;
        try {
            paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
        } catch (DaoException ex) {
            throw new ServletException(ex.getMessage());
        }
        request.setAttribute("paziente", paz);
        rd.forward(request, response);
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
        VisitaSpecialistica vs = null;
        boolean inserito = false;
        
        if(request.getRequestURI().indexOf("new_visite_specialistiche") > 0){
            vs = VisitaSpecialistica.loadFromHttpRequestNew(request, u);
            try {
                if(u.getType() != UtenteType.MEDICO) throw new DaoException("Operazione non ammessa");
                inserito = userDao.Medico().addVisitaSpecialistica(vs);
            } catch (DaoException ex) {
                System.out.println("Errore addVisitaSpecialistica (VisiteSpecServlet) -->\n" + ex.getMessage() + "\n\n");
                inserito = false;
            }
        }
        else if(request.getRequestURI().indexOf("compila_visita_spec") > 0){
            vs = VisitaSpecialistica.loadFromHttpRequestCompila(request, u);
            try {
                if(u.getType() != UtenteType.MEDICO_SPEC) throw new DaoException("Operazione non ammessa");
                inserito = userDao.MedicoSpecialista().compileVisitaSpecialistica(vs, u.getId());
            } catch (DaoException ex) {
                System.out.println("Errore compileVisitaSpecialistica (VisiteSpecServlet) -->\n" + ex.getMessage() + "\n\n");
                inserito = false;
            }
        }
        
        
        if (inserito){
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/dettagli_paziente/visite_specialistiche?id_paziente=" + vs.getId_paziente() + "&r"));
            return;
        }
        

        request.setAttribute("i_visita", vs);
        request.setAttribute("errore", "errore");
        manageNewVisita(request, response);
    }
}
