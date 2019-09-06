package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ticket;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;
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

public class TicketsServlet extends HttpServlet {


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

        if (request.getRequestURI().indexOf("dettagli_ticket") > 0) {
            manageTicektDetails(request, response, u);
            return;
        }

        List<Ticket> tickets;
        String contextPath = getServletContext().getContextPath();

        if (!contextPath.endsWith("/"))
            contextPath += "/";

        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                request.setAttribute("title", "Tickets_paziente"); //per personalizzare il titolo che viene mostrato
                tickets = userDao.getTickets(u.getId());
                for (Ticket t : tickets) {
                    System.out.println("Time: " + t.getTime().toString());
                }
            } else { // Solo paziente può accedere ai propri tickets
                response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));
                return;
            }

            request.setAttribute("tickets", tickets);
            request.setAttribute("page", "tickets");
            RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
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
     * Metodo per gestire la richiesta per visualizzare i dettagli del ticket
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void manageTicektDetails(HttpServletRequest request, HttpServletResponse response, Utente u) throws ServletException, IOException {
        int id_paziente, id_ticket;
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        if (request.getParameter("id_ticket") == null)
            response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/home"));

        try {
            if (u.getType() == UtenteType.PAZIENTE) {
                id_paziente = u.getId();
                id_ticket = Integer.parseInt(request.getParameter("id_ticket"));
                if (id_ticket <= 0) throw new NumberFormatException();
            } else {
                throw new ServletException("id_utente_not_valid");
            }
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }

        Ticket t = null;
        try {
            t = userDao.getTicket(id_paziente, id_ticket);
        } catch (DaoException ex) {
            System.out.println(ex.getMessage());
            throw new ServletException(ex.getMessage());
        } finally {
            if (t == null) throw new ServletException("ticket_not_found");
        }

        // Generazione QRcode in base64
        byte[] qrcodeRaw = QRCode.from(request.getRequestURL().toString() + "?" + request.getQueryString()).withSize(250, 250).to(ImageType.PNG).stream().toByteArray();
        String qrcode = "data:image/png;base64, ".concat(Base64.getEncoder().encodeToString(qrcodeRaw));

        request.setAttribute("qrcode", qrcode);
        request.setAttribute("ticket", t);
        request.setAttribute("title", "view_ticket");
        request.setAttribute("page", "dettagli_ticket");
        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
        rd.forward(request, response);
    }
}
