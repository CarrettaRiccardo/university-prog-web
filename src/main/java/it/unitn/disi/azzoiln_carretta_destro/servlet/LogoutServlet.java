package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * @author Steve
 */
public class LogoutServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        Cookie[] cookies = request.getCookies();     // request is an instance of type 
        //HttpServletRequest
        Boolean found = false;

        // cerca i cookie di "ricordami"
        for (int i = 0; i < cookies.length && found == false; i++) {
            Cookie c = cookies[i];
            if (c.getName().equals("user_token")) {
                found = true;
                c.setValue("");// ne annullo il valore per sicurezza
                c.setMaxAge(0);// cancella
                response.addCookie(c);
            }
        }


        HttpSession session = request.getSession(false);
        session.invalidate();

        if (!response.isCommitted()) {
            response.sendRedirect(response.encodeRedirectURL(contextPath +  "login"));
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
