package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
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
        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        List<Paziente> pazienti = null;
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) 
            contextPath += "/";

        try{
            if(u.getType() == UtenteType.PAZIENTE){                
                //TODO: Decidere se visualizza qualcosa da questa pagina
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "home"));
                return;
            }
            else if(u.getType() == UtenteType.MEDICO ){
                request.setAttribute("title", "Pazienti_medico");
                request.setAttribute("nome", ((Medico)u).getNome() + ((Medico)u).getCognome());
                pazienti = userDao.Medico().getPazienti(u.getId());
            }
            else if(u.getType() == UtenteType.MEDICO_SPEC){
                request.setAttribute("title", "Pazienti_medico_spec");
                request.setAttribute("nome", ((MedicoSpecialista)u).getNome() + ((MedicoSpecialista)u).getCognome());
                pazienti = userDao.MedicoSpecialista().getPazienti(u.getId());
            }
            else{ //sono SSP, non posso vedere le visite delle persone
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "home"));
                return;
            }

            
            request.setAttribute("page", "pazienti");
            request.setAttribute("pazienti", pazienti);  
            RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
            rd.include(request, response);
        }
        catch(IdNotFoundException e){
            throw new ServletException(e.getMessage());  
        }catch(DaoException e){
            throw new ServletException(e);
        }
        catch(NumberFormatException e){
            throw new ServletException(e); //TODO: BAD REQUEST
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
