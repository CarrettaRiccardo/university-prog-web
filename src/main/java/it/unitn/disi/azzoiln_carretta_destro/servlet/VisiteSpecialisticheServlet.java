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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisiteSpecialisticheServlet extends HttpServlet {
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
        if (request.getRequestURI().indexOf("new_visite_specialistiche") > 0) {  //voglio accedere alla pagina per creare una nuova Visita
            manageNewVisita(request, response);
            return;
        }

        Utente u = (Utente) request.getSession(false).getAttribute("utente");

        if (request.getRequestURI().indexOf("compila_visita_spec") > 0) {  //voglio accedere alla pagina per creare una nuova Visita
            manageCompilaVisita(request, response, u);
            return;
        }
        List<VisitaSpecialistica> visite;

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                request.setAttribute("title", "Visite_spec_paziente"); //per personalizzare il titolo che viene mostrato
                visite = userDao.getVisiteSpecialistiche(u.getId());
            } else if (u.getType() == UtenteType.MEDICO) {
                request.setAttribute("title", "Visite_spec_medico");
                request.setAttribute("nome", ((Persona) u).getNome() + ((Persona) u).getCognome());  //per mostrare il nome del medico loggato
                Integer id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                visite = userDao.getVisiteSpecialistiche(id_paziente);
            }
            else if (u.getType() == UtenteType.MEDICO_SPEC) {
                request.setAttribute("title", "Visite_spec_medico");
                request.setAttribute("nome", ((Persona)u).getNome() + ((Persona)u).getCognome());  //per mostrare il nome del medico loggato
                Integer id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                visite = userDao.MedicoSpecialista().getVisiteSpecialistiche(id_paziente, u.getId());
            }
            else{ //sono SSP, non posso vedere le visite delle persone
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));
                return;
            }
            request.setAttribute("visite", visite);
            request.setAttribute("page", "visite_specialistiche");
            request.setAttribute("id_paziente", request.getParameter("id_paziente"));
            RequestDispatcher rd = request.getRequestDispatcher(request.getRequestURI().contains("dettagli_utente") ? "/components/visite_specialistiche.jsp" : "/base.jsp");
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


    private void manageCompilaVisita(HttpServletRequest request, HttpServletResponse response, Utente u) throws ServletException, IOException {
        int id_paziente = -1, id_visita = -1;
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        if (request.getParameter("id_paziente") == null || request.getParameter("id_visita") == null)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));

        try {
            if (u.getType() == UtenteType.PAZIENTE)
                id_paziente = u.getId();
            else
                id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException();
            id_visita = Integer.parseInt(request.getParameter("id_visita"));
            if (id_visita <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }

        VisitaSpecialistica vs = null;
        Double importo_ticket = null;
        try {
            vs = userDao.getVisitaSpecialistica(id_paziente, id_visita);
            importo_ticket = userDao.getImportoTicket(vs.getId_ticket());
        } catch (DaoException ex) {
            System.out.println(ex.getMessage());
            throw new ServletException(ex.getMessage());
        } finally {
            if(vs == null) throw new ServletException("visita_spec_not_found");
            if(importo_ticket == null && !vs.isNew()) throw new ServletException("ticket_not_found");
            if(u.getType() != UtenteType.PAZIENTE && vs.getTime_visita() == null) throw new ServletException("visita_non_fissata"); //il medico/medico_spec non può accedere ad una visita non fissata
            if(/*u.getType() == UtenteType.MEDICO_SPEC &&*/ vs.getTime_visita() != null && vs.getTime_visita().compareTo(new Date()) > 0 ) throw new ServletException("visita_futura");  //nessuno può accedere ad un elemento fissato per il futuro
        }
        
        if(!vs.isNew() || u.getType() != UtenteType.MEDICO_SPEC){ //mostro i campi in readonly, altrimenti compilabili
            request.setAttribute("i_visita", vs);
            request.setAttribute("importo_ticket", importo_ticket);
            request.setAttribute("title", "view_visita_specialistica");
        }
        else if(vs.isDaFissare()){
            request.setAttribute("title", "fissa_visita_specialistica");
        }
        else {
            request.setAttribute("title", "compila_visita_specialistica");
        }

        request.setAttribute("page", "compila_visita_spec");
        request.setAttribute("id_visita", request.getParameter("id_visita"));
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");

        Paziente paz = null;
        try {
            paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
        } catch (DaoException ex) {
            throw new ServletException(ex.getMessage());
        }
        request.setAttribute("paziente", paz);
        request.setAttribute("data", vs.getTime_visita());
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
    private void manageNewVisita(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        request.setAttribute("title", "crea_visita_specialistica");
        request.setAttribute("page", "new_visite_specialistiche");
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");

        Paziente paz = null;
        try {
            paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
        } catch (DaoException ex) {
            throw new ServletException(ex.getMessage());
        }
        request.setAttribute("paziente", paz);
        request.setAttribute("url_rest", Common.getDomain(request) + getServletContext().getInitParameter("url_visite_spec_rest"));  //per url WB per autocompletamento
        rd.forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        VisitaSpecialistica vs = null;
        boolean inserito = false, updateData = false;

        if (request.getRequestURI().indexOf("new_visite_specialistiche") > 0) {
            vs = VisitaSpecialistica.loadFromHttpRequestNew(request, u);
            try {
                if (u.getType() != UtenteType.MEDICO) throw new DaoException("Operazione non ammessa");
                inserito = userDao.Medico().addVisitaSpecialistica(vs);
            } catch (DaoException ex) {
                System.out.println("Errore addVisitaSpecialistica (VisiteSpecServlet) -->\n" + ex.getMessage() + "\n\n");
                inserito = false;
            }
        } else if (request.getRequestURI().indexOf("compila_visita_spec") > 0) {
            vs = VisitaSpecialistica.loadFromHttpRequestCompila(request, u);

            // se un utente sta scegliendo la data della visita specialistica...
            try {
                String data_selez = request.getParameter("datepicker");
                if (u.getType() == UtenteType.PAZIENTE && data_selez != null) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        sdf.setLenient(false);
                        sdf.parse(data_selez);// restituisce una "ParseException" se non e' valida
                    } catch (ParseException ex) {
                        throw new ServletException("invalid_date_exception");
                    }
                    inserito = userDao.Paziente().setDataVisitaSpecialistica(vs.getId(), data_selez);
                    updateData = true;
                } else {
                    if (u.getType() != UtenteType.MEDICO_SPEC) throw new DaoException("Operazione non ammessa");
                    inserito = userDao.MedicoSpecialista().compileVisitaSpecialistica(vs, u.getId());
                }
            } catch (DaoException ex) {
                System.out.println("Errore compileVisitaSpecialistica (VisiteSpecServlet) -->\n" + ex.getMessage() + "\n\n");
                inserito = false;
            }
        }


        if (inserito) {
            try {
                SendEmail.Invia(userDao.getUsername(vs.getId_paziente()), "Un nuovo rapporto di una visita specialistica e' stato inserito",
                        "Gentile utente.<br/>"
                                + "Una visita specialistica con data " + (vs.getTime_visita() != null ?  ((new SimpleDateFormat("dd/MM/yyyy")).format(vs.getTime_visita())) 
                                        : "*da definire*") + " è stata inserita o modificata nella tua scheda paziente."
                                + "<br/>"
                                + "Controlla le tue visite specialistiche per visualizzare i dettagli."
                                + "<br/>" + "<br/>"
                                + "<div style=\"position: absolute; bottom: 5px; font-size: 11px\">Questa è una mail di test ed è generata in modo automatico dal progetto SanityManager</div>");
            } catch (Exception ex) {
                // Ricky; nascondo all'utente se non viene inviata la mail
                System.out.println(ex.getMessage());
            }
            if (updateData)// paziente
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/visite_specialistiche"));
            else // medico_spec
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/dettagli_utente/visite_specialistiche?id_paziente=" + vs.getId_paziente()));
            return;
        }
        else{
            request.setAttribute("i_visita", vs);
            request.setAttribute("errore", "errore");
            if(request.getRequestURI().indexOf("new_visite_specialistiche") > 0)
                manageNewVisita(request, response);
            else if(request.getRequestURI().indexOf("compila_visita_spec") > 0)
                manageCompilaVisita(request, response, u);
        }
    }
}
