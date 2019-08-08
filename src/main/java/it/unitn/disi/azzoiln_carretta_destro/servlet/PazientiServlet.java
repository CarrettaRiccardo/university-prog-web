package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PazientiServlet extends HttpServlet {
    
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
        request.setAttribute("title", "Pazienti");
        request.setAttribute("page", "pazienti");
        
        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        List<Paziente> pazienti = null;
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) 
            contextPath += "/";        
        
        try{
            if(u.getType() == UtenteType.PAZIENTE){                
                //TODO: Decidere se visualizza qualcosa da questa pagina
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/home"));
                return;
            }
            else if(u.getType() == UtenteType.MEDICO ){
                pazienti = userDao.Medico().getPazienti(u.getId());
            }
            else if(u.getType() == UtenteType.MEDICO_SPEC){
                //TODO: Decidere cosa visualizza
            }
            else{ //sono SSP, non posso vedere le visite delle persone
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/home"));
                return;
            }

            request.setAttribute("pazienti", pazienti);
            RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
            rd.include(request, response);
        }
        catch(IdNotFoundException e){
            throw new ServletException(e.getMessage());  //TODO-> mostrare pagina di errore (NOT FOUND PAZIENTE) in base alla stringa per individuare il corretto messaggio
        }catch(DaoException e){
            throw new ServletException(e);
        }
        catch(NumberFormatException e){
            throw new ServletException(e); //TODO-> mostrare pagina di errore BAD REQUEST
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
