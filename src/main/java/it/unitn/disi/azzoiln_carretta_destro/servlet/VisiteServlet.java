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

public class VisiteServlet extends HttpServlet {

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
        if (request.getRequestURI().indexOf("new_visita") > 0) {  //voglio accedere alla pagina per creare una nuova Visita
            int id_paziente = -1;
            String contextPath = getServletContext().getContextPath();
            if (!contextPath.endsWith("/"))
                contextPath += "/";
            if (request.getParameter("id_paziente") == null)
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/home"));

            try {
                id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                if (id_paziente <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                throw new ServletException("id_paziente_not_valid");
            } catch (Exception e) {
                throw new ServletException();
            }

            request.setAttribute("title", "crea_visita");
            request.setAttribute("page", "new_visita");
            RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");

            Paziente paz = null;
            try {
                paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
            } catch (DaoException ex) {
                throw new ServletException(ex.getMessage());
            }
            request.setAttribute("paziente", paz);
            rd.forward(request, response);
            return;
        }


        request.setAttribute("title", "Visite");
        request.setAttribute("page", "visite");

        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        List<Visita> visite;

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                visite = userDao.getVisite(u.getId());
            } else if (u.getType() == UtenteType.MEDICO || u.getType() == UtenteType.MEDICO_SPEC) {
                Integer id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                visite = userDao.getVisite(id_paziente);
                for(Visita v : visite){
                    System.out.println(v.getId());
                }
            }
            else{ //sono SSP, non posso vedere le visite delle persone
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/home"));
                return;
            }

            //request.setAttribute("visite", visite);
            RequestDispatcher rd = request.getRequestDispatcher(request.getRequestURI().contains("dettagli_paziente") ? "/components/visite.jsp" : "/base.jsp");
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
     * Prende i parametri da new_visita e crea una nuova Visita
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String anamnesi = request.getParameter("anamnesi");
        int id_paziente = -1;

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        if (request.getParameter("id_paziente") == null)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/home"));
        try {
            id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ServletException("id_paziente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }

        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        Visita v = new Visita(anamnesi, id_paziente, u.getId());
        boolean inserito;
        try {
            inserito = userDao.Medico().addVisita(v);
        } catch (DaoException ex) {
            inserito = false;
        }
        
        /*request.setAttribute("title","Visite");               NON POSSIBILE PER LOOP
        request.setAttribute("page", "paziente_dettagli");
        request.setAttribute("subpage", "visite");
        request.setAttribute("result", "ok"); //per mostre una conferma di creazione visita
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
        rd.forward(request, response);*/
        if (inserito)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/dettagli_paziente/visite?id_paziente=" + id_paziente + "&r"));
        else {
            Paziente paz = null;
            try {
                paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
            } catch (DaoException ex) {
                throw new ServletException(ex.getMessage());
            }

            request.setAttribute("title", "crea_visita");
            request.setAttribute("page", "new_visita");
            request.setAttribute("i_anamnesi", anamnesi);
            request.setAttribute("paziente", paz);

            RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
            rd.include(request, response);
        }
    }

}
