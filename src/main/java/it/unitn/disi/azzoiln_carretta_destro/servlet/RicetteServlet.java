package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.*;
import it.unitn.disi.azzoiln_carretta_destro.utility.Common;
import it.unitn.disi.azzoiln_carretta_destro.utility.SendEmail;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RicetteServlet extends HttpServlet {

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
        Utente u = (Utente) request.getSession(false).getAttribute("utente");

        if (request.getRequestURI().indexOf("new_ricette") > 0) {  //voglio accedere alla pagina per creare una nuova Ricetta
            manageNewRicetta(request, response, u);
            return;
        } else if (request.getRequestURI().indexOf("dettagli_ricetta") > 0) {
            manageRicettaDetails(request, response, u);
            return;
        }

        List<Ricetta> ricette;
        String contextPath = getServletContext().getContextPath();

        if (!contextPath.endsWith("/"))
            contextPath += "/";

        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                request.setAttribute("title", "Ricette_paziente"); //per personalizzare il titolo che viene mostrato
                ricette = userDao.getRicette(u.getId());
            } else if (u.getType() == UtenteType.MEDICO) {
                request.setAttribute("title", "Ricette_medico");
                request.setAttribute("nome", ((Medico) u).getNome() + ((Medico) u).getCognome());  //per mostrare il nome del medico loggato
                Integer id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                ricette = userDao.getRicette(id_paziente);
            } else { //sono SSP, non posso vedere le visite delle persone
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));
                return;
            }

            request.setAttribute("ricette", ricette);
            request.setAttribute("page", "ricette");
            request.setAttribute("id_paziente", request.getParameter("id_paziente"));
            RequestDispatcher rd = request.getRequestDispatcher(request.getRequestURI().contains("dettagli_utente") ? "/components/ricette.jsp" : "/base.jsp");
            rd.include(request, response);
        } catch (IdNotFoundException e) {
            throw new ServletException("utente_not_found");
        } catch (DaoException e) {
            throw new ServletException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }
    }


    /**
     * Metodo per gestire la richiesta per visualizzare il riassunto della ricetta.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void manageRicettaDetails(HttpServletRequest request, HttpServletResponse response, Utente u) throws ServletException, IOException {
        int id_paziente, id_ricetta;
        String CF_paziente;
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        if (request.getParameter("id_paziente") == null || request.getParameter("id_ricetta") == null)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));

        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                id_paziente = u.getId();
                CF_paziente = ((Paziente) u).getCf();
            } else if (u.getType() == UtenteType.MEDICO) {
                id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
                if (id_paziente <= 0) throw new NumberFormatException();
                Paziente paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
                CF_paziente = paz.getCf();
            } else {
                throw new ServletException("id_utente_not_valid");
            }
            id_ricetta = Integer.parseInt(request.getParameter("id_ricetta"));
            if (id_ricetta <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }

        Ricetta r = null;
        try {
            r = userDao.getRicetta(id_paziente, id_ricetta);
        } catch (DaoException ex) {
            System.out.println(ex.getMessage());
            throw new ServletException(ex.getMessage());
        } finally {
            if (r == null) throw new ServletException("ricetta_not_found");
        }

        // Generazione QRcode in base64
        byte[] qrcodeRaw = QRCode.from(request.getRequestURL().toString() + "?" + request.getQueryString()).withSize(250, 250).to(ImageType.PNG).stream().toByteArray();
        String qrcode = "data:image/png;base64, ".concat(Base64.getEncoder().encodeToString(qrcodeRaw));

        request.setAttribute("qrcode", qrcode);
        request.setAttribute("ricetta", r);
        request.setAttribute("CF_paziente", CF_paziente);
        request.setAttribute("title", "view_ricetta");
        request.setAttribute("page", "dettagli_ricetta");

        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
        rd.forward(request, response);
    }


    /**
     * Effetua il redirect a new_ricetta impostando e controllando i relativi parametri
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void manageNewRicetta(HttpServletRequest request, HttpServletResponse response, Utente u) throws ServletException, IOException {
        int id_paziente = -1;
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";
        if (request.getParameter("id_paziente") == null)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));

        try {
            if (u.getType() == UtenteType.PAZIENTE) //così il Paziente non vede il suo ID nell'URL
                id_paziente = u.getId();
            else
                id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }

        request.setAttribute("title", "crea_ricetta");
        request.setAttribute("page", "new_ricette");
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");

        Paziente paz = null;
        try {
            paz = (Paziente) userDao.getByPrimaryKey(id_paziente, "paziente");
        } catch (DaoException ex) {
            throw new ServletException(ex.getMessage());
        }

        request.setAttribute("paziente", paz);
        request.setAttribute("url_rest", Common.getDomain(request) + getServletContext().getInitParameter("url_farmaci_rest"));  //per url WB per autocompletamento
        rd.forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        Ricetta r = Ricetta.loadFromHttpRequest(request, u);
        boolean inserito;
        try {
            if (u.getType() != UtenteType.MEDICO) throw new DaoException("Operazione non ammessa");
            inserito = userDao.Medico().addRicetta(r);
        } catch (DaoException ex) {
            inserito = false;
        }

        if (inserito) {
            try {
                SendEmail.Invia(userDao.getUsername(r.getId_paziente()), "Una nuova ricetta e' stata inserita",
                        "Gentile utente.<br/>"
                                + "Una nuova ricetta è stata aggiunta nella tua scheda dal tuo medico di base."
                                + "<br/>"
                                + "Controlla le tue ricette per visualizzare i dettagli."
                                + "<br/>" + "<br/>"
                                + "<div style=\"position: absolute; bottom: 5px; font-size: 11px\">Questa è una mail di test ed è generata in modo automatico dal progetto SanityManager</div>");
            } catch (Exception ex) {
                // Ricky; nascondo all'utente se non viene inviata alla mail
                Logger.getLogger(VisiteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/dettagli_utente/ricette?id_paziente=" + r.getId_paziente()));
            return;
        }

        request.setAttribute("i_ricetta", r);
        request.setAttribute("errore", "errore");  //setto parametro per mostrare popup-errore

        manageNewRicetta(request, response, u);  //uso il metodo già definire per gestire il redirect a new_ricetta settando dei parametri aggiuntivi
    }


}
