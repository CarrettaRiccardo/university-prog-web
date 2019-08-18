package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.PazienteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Prenotazione;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrenotazioniServlet extends HttpServlet {
    
    private UtenteDao userDao;
    //private PazienteDao pazDao;

    @Override
    public void init() throws ServletException {
        DaoFactory daoFactory = (DaoFactory) getServletContext().getAttribute("daoFactory"); //Steve ho tolto super.
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UtenteDao.class);
            //pazDao = daoFactory.getDAO(PazienteDao.class);
        } catch (DaoFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("title", "Prenotazioni");
        request.setAttribute("page", "prenotazioni");
        
        Utente u = (Utente) request.getSession(true).getAttribute("utente");
        Paziente p = null;
        
        if (u.getType() == UtenteType.PAZIENTE){// TODO  PazienteDao
            p = (Paziente) u;
            //Medico m = pazDao.getMedico(p.getId_medico());
            String nomeMedico = "";//m.getNome() + " " + m.getCognome();
            request.setAttribute("nomeMedico", nomeMedico);
        }
                    
        if (request.getParameter("date") != null && !request.getParameter("date").isEmpty()){
            try {
                String date = request.getParameter("date");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
                sdf.parse(date);// restituisce una "ParseException" se non e' valida
                
                Integer idMedico = null;
                if (p != null){
                    idMedico = p.getId_medico();

                    if (request.getParameter("orario") != null && !request.getParameter("orario").isEmpty()){
                        Integer ora = Integer.parseInt(request.getParameter("orario")); 
                        if (ora <= 18 && ora >= 8){
                            userDao.Paziente().newPrenotazione(new Prenotazione(u.getId(), idMedico, date + " " + ora + ":00"));
                        }
                        else{
                            throw new NumberFormatException();
                        }
                    }
                    List<Prenotazione> l = new LinkedList<Prenotazione>(userDao.Paziente().getPrenotazioni(request.getParameter("date"), idMedico));
                    request.setAttribute("reservations", l);
                }
                request.setAttribute("date", date);
            } catch (ParseException ex){
                throw new ServletException("invalid_date_exception");
            } catch (NumberFormatException ex){
                throw new ServletException("invalid_hour_exception");
            } catch (Exception ex) {
                throw new ServletException("retrieving_reservations_error");
            }
        }

        RequestDispatcher rd = request.getRequestDispatcher("/base.jsp");
        rd.include(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
