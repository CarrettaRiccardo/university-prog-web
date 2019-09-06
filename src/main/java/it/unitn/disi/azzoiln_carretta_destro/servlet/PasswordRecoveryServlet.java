package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.utility.SendEmail;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Steve
 */
public class PasswordRecoveryServlet extends HttpServlet {

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
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        String where = "";

        try {
            String token = request.getParameter("token");
            String newPassword = request.getParameter("newpassword");
            String newPasswordConfirm = request.getParameter("newpasswordconfirm");
            if (token != null && newPassword != null) {
                if (newPassword.equals(newPasswordConfirm)){
                    if (userDao.updatePasswordAndRemoveToken(token, newPassword)) {
                        where = "?success=true";
                    } else {
                        where = "?hasToken=true&recovery_error=invalid_token";
                    }
                }
                else{
                    where = "?hasToken=true&recovery_error=password";
                }
            } else {
                String email = request.getParameter("email");
                if (userDao.existsUsername(email) != null) {
                    if (userDao.getPasswordToken(email) == null) {
                        Boolean success = userDao.insertPasswordToken(email);
                        if (success) {
                            SendEmail.Invia(email, "Reset Password",
                                    "<div style=\"text-align: center\">"
                                            + "<p style=\"font-size: 20px\">Il token per il reset della password e':</p>"
                                            + "<br/>"
                                            + "<b style=\"font-size: 24px\">" + userDao.getPasswordToken(email) + "</b>"
                                            + "</div>");
                            where = "?hasToken=true";
                        }
                    } else {
                        where = "?recovery_error=existing_token";
                    }
                } else {
                    where = "?recovery_error=user";
                }
            }
        } catch (MessagingException ex) {
            where = "?recovery_error=sending";
        } catch (Exception ex) {
            where = "?recovery_error=";// errore del sistema
        }

        response.sendRedirect(response.encodeRedirectURL(contextPath + "password_recovery" + where));
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
        doGet(request, response);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
