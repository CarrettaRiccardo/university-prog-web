package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.*;
import it.unitn.disi.azzoiln_carretta_destro.utility.Common;
import it.unitn.disi.azzoiln_carretta_destro.utility.SendEmail;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EsamiServlet extends HttpServlet {
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
        if (request.getRequestURI().indexOf("new_esami") > 0) {  //voglio accedere alla pagina per creare un nuovo Esame
            manageNewEsame(request, response);
            return;
        }

        Utente u = (Utente) request.getSession(false).getAttribute("utente");

        if (request.getRequestURI().indexOf("compila_esame") > 0) {  //voglio accedere alla pagina per creare una nuova Visita
            manageCompilaEsame(request, response, u);
            return;
        }
        List<Esame> esami;

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                request.setAttribute("title", "Esami_paziente"); //per personalizzare il titolo che viene mostrato
                esami = userDao.getEsami(u.getId());
            } else if (u.getType() == UtenteType.MEDICO || u.getType() == UtenteType.MEDICO_SPEC) {
                request.setAttribute("title", "Esami_medico");
                request.setAttribute("nome", ((Persona) u).getNome() + ((Persona) u).getCognome());  //per mostrare il nome del medico loggato
                Integer id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                esami = userDao.getEsami(id_paziente);
            } else if (u.getType() == UtenteType.SSP) {
                request.setAttribute("title", "Esami_ssp");
                request.setAttribute("nome", ((Ssp) u).getNome());  //per mostrare il nome del SSP
                esami = userDao.getSspEsami(u.getId());
            } else { //sono SSP, non posso vedere le visite delle persone
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));
                return;
            }

            request.setAttribute("esami", esami);
            request.setAttribute("page", "esami");
            request.setAttribute("id_paziente", request.getParameter("id_paziente"));
            RequestDispatcher rd = request.getRequestDispatcher(request.getRequestURI().contains("dettagli_utente") ? "/components/esami.jsp" : "/base.jsp");
            rd.include(request, response);
        } catch (IdNotFoundException e) {
            throw new ServletException("utente_not_found");
        } catch (DaoException e) {
            throw new ServletException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n\n\n");
            throw new ServletException(e.getMessage());
        }
    }


    private void manageCompilaEsame(HttpServletRequest request, HttpServletResponse response, Utente u) throws ServletException, IOException {
        int id_paziente = -1, id_esame = -1;
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        if (request.getParameter("id_paziente") == null || request.getParameter("id_esame") == null)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));

        try {
            if (u.getType() == UtenteType.PAZIENTE) //così il Paziente non vede il suo ID nell'URL
                id_paziente = u.getId();
            else
                id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException();
            id_esame = Integer.parseInt(request.getParameter("id_esame"));
            if (id_esame <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }

        Esame e = null;
        try {
            e = userDao.getEsame(id_paziente, id_esame);
        } catch (DaoException ex) {
            System.out.println(ex.getMessage());
            throw new ServletException(ex.getMessage());
        } finally {
            if (e == null) throw new ServletException("visita_spec_not_found");
        }

        if (!e.isNew() || u.getType() != UtenteType.MEDICO_SPEC) { //mostro i campi in readonly, altrimenti compilabili
            request.setAttribute("i_esame", e);
            request.setAttribute("title", "view_esame");
        } else {
            request.setAttribute("title", "compila_esame");
        }


        request.setAttribute("page", "compila_esame");
        request.setAttribute("id_esame", request.getParameter("id_esame"));  //per inserire tale valore come campo nascosto nell' HHTML (per averlo come parametro in POST per la compilazione)
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");

        Paziente paz = null;
        try {
            paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
        } catch (DaoException ex) {
            throw new ServletException(ex.getMessage());
        }
        request.setAttribute("paziente", paz);
        request.setAttribute("data", e.getTime_esame());
        rd.forward(request, response);
    }


    /**
     * Come in RicetteServlet.java
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void manageNewEsame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id_paziente = -1;
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        if (request.getParameter("id_paziente") == null)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));

        try {
            id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }

        request.setAttribute("title", "crea_esame");
        request.setAttribute("page", "new_esami");
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");

        Paziente paz = null;
        try {
            paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
        } catch (DaoException ex) {
            throw new ServletException(ex.getMessage());
        }
        request.setAttribute("paziente", paz);
        request.setAttribute("url_rest", Common.getDomain(request) + getServletContext().getInitParameter("url_esami_rest"));  //per url WB per autocompletamento
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        Esame v = Esame.loadFromHttpRequest(request, u);

        boolean inserito = false, updateData = false;
        try {
            // se un utente sta scegliendo la data della visita specialistica...
            String data_selez = request.getParameter("datepicker");
            if (u.getType() == UtenteType.PAZIENTE && data_selez != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    sdf.parse(data_selez);// restituisce una "ParseException" se non e' valida
                } catch (ParseException ex) {
                    throw new ServletException("invalid_date_exception");
                }
                inserito = userDao.Paziente().setDataEsame(v.getId_esame(), data_selez);
                updateData = true;
            } else {
                if (u.getType() != UtenteType.MEDICO) throw new DaoException("Operazione non ammessa");
                inserito = userDao.Medico().addEsame(v);
            }
        } catch (DaoException ex) {
            System.out.println("Errore addEsame (EsamiServlet) -->\n" + ex.getMessage() + "\n\n");
            inserito = false;
        }

        if (inserito) {
            try {
                SendEmail.Invia(userDao.getUsername(v.getId_paziente()), "Un nuovo esame e' stato inserito",
                        "Gentile utente.<br/>"
                                + "Un esame con data " + (v.getTime_esame() != null ? ((new SimpleDateFormat("dd/MM/yyyy")).format(v.getTime_esame()))
                                : "*da definire*") + " è stato inserito o modificato nella tua scheda paziente."
                                + "<br/>"
                                + "Controlla i tuoi esami per visualizzare i dettagli."
                                + "<br/>" + "<br/>"
                                + "<div style=\"position: absolute; bottom: 5px; font-size: 11px\">Questa è una mail di test ed è generata in modo automatico dal progetto SanityManager</div>");
            } catch (Exception ex) {
                // Ricky; nascondo all'utente se non viene inviata alla mail
                Logger.getLogger(VisiteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (updateData)// paziente
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/esami"));
            else // medico
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/dettagli_utente/esami?id_paziente=" + v.getId_paziente() + "&r"));
            return;
        }


        //request.setAttribute("i_anamnesi", v.getAnamnesi());
        request.setAttribute("errore", "errore");
        manageNewEsame(request, response);
    }
}
