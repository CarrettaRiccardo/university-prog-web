package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;
import it.unitn.disi.azzoiln_carretta_destro.utility.Common;
import it.unitn.disi.azzoiln_carretta_destro.utility.SendEmail;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.mail.MessagingException;

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
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        String where = "";

        try{
            String token = request.getParameter("token");
            String newPassword = request.getParameter("newpassword");
            if (token != null && newPassword != null){
                if (userDao.updatePasswordAndRemoveToken(token, newPassword)){
                    where = "?success=true";
                }
                else{
                    where = "?hasToken=true&recovery_error=invalid_token";
                }
            }
            else{
                String email = request.getParameter("email");
                if (userDao.existsUsername(email) != null){
                    if (userDao.getPasswordToken(email) == null) {
                        Boolean success = userDao.insertPasswordToken(email);
                        if (success){
                            SendEmail.Invia(email, "Reset Password",
                                "<div style=\"text-align: center\">"
                                    + "<p style=\"font-size: 20px\">Il token per il reset della password e':</p>"
                                    + "<br/>"
                                    + "<b style=\"font-size: 24px\">" + userDao.getPasswordToken(email) + "</b>"
                                + "</div>");
                            where = "?hasToken=true";
                        }
                    }
                    else{
                        where = "?recovery_error=existing_token";
                    }
                }
                else{
                    where = "?recovery_error=user";
                }
            }
        } catch (MessagingException ex){
            where = "?recovery_error=sending";
        } catch (Exception ex){
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
