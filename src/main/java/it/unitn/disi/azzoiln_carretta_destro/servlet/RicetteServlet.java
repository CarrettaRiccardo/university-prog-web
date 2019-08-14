package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.MedicoDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Farmaci;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * 
 * @author Steve
 */
public class RicetteServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURI().indexOf("new_ricetta") > 0) {  //voglio accedere alla pagina per creare una nuova Ricetta
            manageNewRicetta(request, response);
            return;
        }

        request.setAttribute("page", "ricette");
        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        List<Ricetta> ricette;

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                request.setAttribute("title", "Ricette_paziente"); //per personalizzare il titolo che viene mostrato
                ricette = userDao.getRicette(u.getId());
            } else if (u.getType() == UtenteType.MEDICO || u.getType() == UtenteType.MEDICO_SPEC) {
                request.setAttribute("title", "Ricette_medico");
                request.setAttribute("nome", ((Medico)u).getNome() + ((Medico)u).getCognome());  //per mostrare il nome del medico loggato
                Integer id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                ricette = userDao.getRicette(id_paziente);
                for(Ricetta v : ricette){
                    System.out.println("Ricetta: " + v.getId());
                }
            }
            else{ //sono SSP, non posso vedere le visite delle persone
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));
                return;
            }

            request.setAttribute("ricette", ricette);
            RequestDispatcher rd = request.getRequestDispatcher(request.getRequestURI().contains("dettagli_paziente") ? "/components/ricette.jsp" : "/base.jsp");
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
     * Effetua il redirect a new_ricetta impostando e controllando i relativi parametri
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    private void manageNewRicetta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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

        request.setAttribute("title", "crea_ricetta");
        request.setAttribute("page", "new_ricetta");
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");

        Paziente paz = null;
        try {
            paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
        } catch (DaoException ex) {
            throw new ServletException(ex.getMessage());
        }
            
        request.setAttribute("paziente", paz);
        request.setAttribute("url_rest", getServletContext().getInitParameter("url_farmaci_rest"));  //per url WB per autocompletamento
        rd.forward(request, response);
    }
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";        

        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        Ricetta r = Ricetta.loadFromHttpRequest(request, u);
        boolean inserito;
        try {
            inserito = userDao.Medico().addRicetta(r);
        } catch (DaoException ex) {
            inserito = false;
        }
        
        if (inserito){
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/dettagli_paziente/ricette?id_paziente=" + r.getId_paziente() + "&r"));
            return;
        }
        
        request.setAttribute("i_qta", r.getQuantita());
        request.setAttribute("errore", "errore");  //setto parametro per mostrare popup-errore
            
        manageNewRicetta(request, response);  //uso il metodo già definire per gestire il redirect a new_ricetta settando dei parametri aggiuntivi        
    }


}
