package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.rmi.ServerException;

/**
 * @author Steve
 */
public class ChooseServlet extends HttpServlet {

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
        String mod = request.getParameter("m");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        HttpSession session = request.getSession(false);

        Utente u = (Utente) session.getAttribute("utente");

        if (u instanceof Medico || u instanceof MedicoSpecialista || u instanceof Ssp || u instanceof Paziente){ //per cambiare modalit?? deve fare il logout
            request.getSession(false).setAttribute("choose", "1");
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/home"));
            return;
        }

        if (mod == null || u == null || (!mod.equals("m") && !mod.equals("p"))) {  //mostro un errore anche se l' utente loggato ?? gi?? stato riconosciuto come paziente o come ssp
            response.sendRedirect(response.encodeRedirectURL(contextPath + "login?login_error=service"));
            return;
        } else {
            try {
                Persona p = ((Persona) u);
                if (mod.equals("m")) {  //scelta modalit?? medico. La differenziazione tra medico e medico_spec la faccio qua
                    switch (p.getRuolo()) {
                        case "medico": session.setAttribute("utente", getMedico(p)); break;//creo un oggetto Medico(leggendo i dati che mancano  usando getByPrimaryKey) e lo salvo nella sessione al posto di utente
                        case "medico_spec": session.setAttribute("utente", getMedicoSpecialista(p)); break;
                        default: session.invalidate(); response.sendRedirect(response.encodeRedirectURL(contextPath + "login?login_error=service")); System.out.println(p.getRuolo()); return; //errore, operazione non consentita
                    }
                } else { //creo un oggetto Paziente e lo salvo nella sessione al posto di utente
                    session.setAttribute("utente", getPaziente(p));
                }
            } catch (DaoException ex) {
                throw new ServerException("Errore il doGet ChooseServlet", ex);
            }
        }
        response.sendRedirect(response.encodeRedirectURL(contextPath + "app/home"));
    }


    private Paziente getPaziente(Persona p) throws DaoException {
        Paziente m = (Paziente) userDao.getByPrimaryKey(p.getId(), "paziente");
        return m;
    }

    private Medico getMedico(Persona p) throws DaoException {
        Medico m = (Medico) userDao.getByPrimaryKey(p.getId(), "medico");
        return m;
    }

    private MedicoSpecialista getMedicoSpecialista(Persona p) throws DaoException {
        MedicoSpecialista m = (MedicoSpecialista) userDao.getByPrimaryKey(p.getId(), "medico_spec");
        return m;
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
