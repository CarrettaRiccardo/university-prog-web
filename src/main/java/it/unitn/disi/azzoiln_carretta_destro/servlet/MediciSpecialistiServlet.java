package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MediciSpecialistiServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        Utente u = (Utente) request.getSession(false).getAttribute("utente");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) contextPath += "/";

        try {
            if (u.getType() != UtenteType.SSP) { // Non sono SSP, non posso vedere la lista di medici specialisti
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "home"));
                return;
            }

            List<MedicoSpecialista> mediciSpecialsti = userDao.Ssp().getMediciSpecialisti(u.getProvincia());
            request.setAttribute("title", "Medici specialisti della provincia di " + userDao.Ssp().getNomeProvincia(u.getProvincia()));
            request.setAttribute("page", "medici_specialisti");
            request.setAttribute("mediciSpecialsti", mediciSpecialsti);
            RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
            rd.include(request, response);
        } catch (IdNotFoundException e) {
            throw new ServletException(e.getMessage());
        } catch (DaoException e) {
            throw new ServletException(e);
        } catch (NumberFormatException e) {
            throw new ServletException(e); //TODO: BAD REQUEST
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
