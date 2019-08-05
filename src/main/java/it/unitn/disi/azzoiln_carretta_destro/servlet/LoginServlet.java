package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Steve
 */
public class LoginServlet extends HttpServlet {

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


    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("username");
        String password = request.getParameter("password");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        if (email == null || password == null) {
            response.sendRedirect(response.encodeRedirectURL(contextPath + "login?login_error=user"));
        }

        try {
            Utente u = (Utente) userDao.login(email, password);  //Non capisco perchè sia necessario il cast, se qualcuno lo sa lo dica a Steve :)
            String where = "";
            switch (u.getRes()) {
                case -2:
                    where = "login?login_error=pwd";
                    break;
                case -1:
                    where = "login?login_error=user";
                    break;
                case 0:
                    where = "app/home";
                    request.getSession(true).setAttribute("utente", u);
                    break;
                case 2:
                    where = "app/home";
                    request.getSession(true).setAttribute("utente", u);
                    break;
                case 1:
                    where = "app/home";
                    request.getSession(true).setAttribute("utente", u);
                    break;
                case -3:
                default:
                    where = "login?login_error=service";
                    break;
            }

            response.sendRedirect(response.encodeRedirectURL(contextPath + where));
        } catch (DaoException ex) {
            request.getServletContext().log("Impossible to retrieve the user", ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }


}
