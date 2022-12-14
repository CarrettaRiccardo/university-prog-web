package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.*;
import it.unitn.disi.azzoiln_carretta_destro.utility.SendEmail;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        response.setContentType("text/html;charset=UTF-8");
        Utente u = (Utente) request.getSession(false).getAttribute("utente");

        if (request.getRequestURI().indexOf("new_visite") > 0) {  //voglio accedere alla pagina per creare una nuova Visita
            manageNewVisita(request, response, u);
            return;
        }
        List<Visita> visite;
        String contextPath = getServletContext().getContextPath();

        if (!contextPath.endsWith("/"))
            contextPath += "/";

        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                request.setAttribute("title", "Visite_paziente"); //per personalizzare il titolo che viene mostrato
                visite = userDao.getVisite(u.getId());
            } else if (u.getType() == UtenteType.MEDICO || u.getType() == UtenteType.MEDICO_SPEC) {
                request.setAttribute("title", "Visite_medico");
                request.setAttribute("nome", ((Persona) u).getNome() + ((Persona) u).getCognome());  //per mostrare il nome del medico loggato
                Integer id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                visite = userDao.getVisite(id_paziente);
            } else { //sono SSP, non posso vedere le visite delle persone
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));
                return;
            }

            request.setAttribute("visite", visite);
            request.setAttribute("page", "visite");
            request.setAttribute("id_paziente", request.getParameter("id_paziente"));
            RequestDispatcher rd = request.getRequestDispatcher(request.getRequestURI().contains("dettagli_utente") ? "/components/visite.jsp" : "/base.jsp");
            rd.include(request, response);
        } catch (IdNotFoundException e) {
            throw new ServletException("utente_not_found");
        } catch (DaoException e) {
            throw new ServletException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServletException();
        }
    }


    /**
     * Come in RicetteServlet.java
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void manageNewVisita(HttpServletRequest request, HttpServletResponse response, Utente u) throws ServletException, IOException {
        int id_paziente = -1, id_visita = -1;
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        if (request.getParameter("id_paziente") == null)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));

        try {
            if (u.getType() == UtenteType.PAZIENTE) //cos?? il Paziente non vede il suo ID nell'URL
                id_paziente = u.getId();
            else
                id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }

        if (request.getParameter("id_visita") != null) { //mostro il riepilogo della visita
            request.setAttribute("title", "view_visita");
            request.setAttribute("page", "new_visite");
            System.out.println("rerererer");
            try {
                id_visita = Integer.parseInt(request.getParameter("id_visita"));
                if (id_visita <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                throw new ServletException("id_utente_not_valid");
            } catch (Exception e) {
                throw new ServletException();
            }

            Visita v = null;
            try {
                v = userDao.getVisita(id_paziente, id_visita);
                System.out.println("La visita da mostrare ->" + v.getAnamnesi());
            } catch (DaoException ex) {
                Logger.getLogger(VisiteServlet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (v == null)
                    throw new ServletException("visita_not_found"); //se non trova id_visita o la query che specifica id_paziente=? AND id_visita non ha ritornato nulla
            }
            request.setAttribute("i_visita", v);
        } else { //Un medico che vuole creare una visita. Il controllo che sia davvero un medico viene fatto quando cerca di salvare i dati
            request.setAttribute("title", "crea_visita");
            request.setAttribute("page", "new_visite");
        }

        /*request.setAttribute("title", "crea_visita");
        request.setAttribute("page", "new_visite");*/
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");

        Paziente paz = null;
        try {
            paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
        } catch (DaoException ex) {
            throw new ServletException(ex.getMessage());
        }

        request.setAttribute("paziente", paz);
        rd.forward(request, response);
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
        response.setContentType("text/html;charset=UTF-8");
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        Visita v = Visita.loadFromHttpRequest(request, u);

        boolean inserito;
        try {
            if (u.getType() != UtenteType.MEDICO) throw new DaoException("Operazione non ammessa");
            inserito = userDao.Medico().addVisita(v);
        } catch (DaoException ex) {
            System.out.println(ex.getMessage());
            inserito = false;
        }

        if (inserito) {
            try {
                SendEmail.Invia(userDao.getUsername(v.getId_paziente()), "Un nuovo rapporto di una visita e' stato inserito",
                        "Gentile utente.<br/>"
                        + "Una visita con data " + (v.getTime() != null ? ((new SimpleDateFormat("dd/MM/yyyy")).format(v.getTime())) 
                                : "*da definire*") + " ?? stata inserita o modificata nella tua scheda paziente."
                        + "<br/>"
                        + "Controlla le tue visite per visualizzare i dettagli."
                        + "<br/>" + "<br/>"
                        + "<div style=\"position: absolute; bottom: 5px; font-size: 11px\">Questa ?? una mail di test ed ?? generata in modo automatico dal progetto SanityManager</div>");
            } catch (Exception ex) {
                // Ricky; nascondo all'utente se non viene inviata alla mail
                System.out.println(ex.getMessage());
            }
            request.getSession(false).setAttribute("success", "1");
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/dettagli_utente/visite?id_paziente=" + v.getId_paziente()));
            return;
        }


        request.setAttribute("i_visita", v);
        request.setAttribute("errore", "errore");
        manageNewVisita(request, response, u);
    }


}
