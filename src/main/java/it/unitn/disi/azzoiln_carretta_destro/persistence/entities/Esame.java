/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Steve
 */
public class Esame extends Prescrizione {

    private int id_esame, id_ticket, id_ssp; //id_esame è l'id tra i vari esami possibili prescrivibili
    private String risultato, nome_esame;
    private Date time_esame,data_nascita_paziente;
    private String nome_paziente;

    public Esame(int id_esame, int id_ticket, int id_ssp, String risultato, Date time_esame, int id_presc, int id_paziente, int id_medico, Date time, String nome_esame) {
        super(id_presc, id_paziente, id_medico, time);
        this.id_esame = id_esame;
        this.id_ticket = id_ticket;
        this.id_ssp = id_ssp;
        this.risultato = risultato;
        this.time_esame = time_esame;
        this.nome_esame = nome_esame;
    }

    public Esame(int id_esame, int id_ticket, int id_ssp, String risultato, Date time_esame, int id_presc, int id_paziente, int id_medico, Date time, String nome_esame, String nome_paziente) {
        super(id_presc, id_paziente, id_medico, time);
        this.id_esame = id_esame;
        this.id_ticket = id_ticket;
        this.id_ssp = id_ssp;
        this.risultato = risultato;
        this.time_esame = time_esame;
        this.nome_esame = nome_esame;
        this.nome_paziente = nome_paziente;
    }

    private Esame(Integer id_esame, int id_paziente, int id_medico) {
        super(-1, id_paziente, id_medico, null);
        this.id_esame = id_esame;
    }

    public String getNome_paziente(){
        return nome_paziente;
    }

    public int getId_esame() {
        return id_esame;
    }

    public int getId_ticket() {
        return id_ticket;
    }

    public int getId_ssp() {
        return id_ssp;
    }

    public String getRisultato() {
        return risultato;
    }

    public String getNome_esame() {
        return nome_esame;
    }
    
    public void setNome_esame(String nome_esame) {
        this.nome_esame = nome_esame;
    }

    public Date getTime_esame() {
        return time_esame;
    }

    /**
     * @return T Se è stato creato dal medico ma non è stata ancora selezionata la data_visita dal Paziente
     */
    public boolean isNew() {
        return id_ssp <= 0; //Perchè null diventa 0 da Integer a int
    }

    /**
     * @return T Se è stata scelta una data dal Paziente
     */
    public boolean isDaFissare() {
        return time_esame == null;
    }

    public Date getData_nascita_paziente() {
        return data_nascita_paziente;
    }

    public void setData_nascita_paziente(Date data_nascita_paziente) {
        this.data_nascita_paziente = data_nascita_paziente;
    }

    /**
     * Esegue tutta la logica di gestione parametri e controllo valori non validi
     *
     * @param req
     * @param u   Utente di sessione per prelevare id_medico
     * @return Oggetto Esame
     * @throws javax.servlet.ServletException
     */
    public static Esame loadFromHttpRequestNew(final HttpServletRequest request, final Utente u) throws ServletException {
        int id_paziente = -1;
        Integer id_esame = -1;

        if (request.getParameter("id_paziente") == null || request.getParameter("id_esame") == null)
            throw new ServletException("bad_request");
        try {
            System.out.println("ssaasas\n\n");
            id_esame = Integer.parseInt(request.getParameter("id_esame"));
            id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException("id_utente_not_valid");
            if (id_esame <= 0) throw new NumberFormatException("id_esame_not_valid");
        } catch (NumberFormatException e) {
            throw new ServletException(e.getMessage());
        } catch (Exception e) {
            throw new ServletException();
        }

        return new Esame(id_esame, id_paziente, u.getId());
    }
    
    
    public static Esame loadFromHttpRequestCompila(HttpServletRequest request, Utente u) throws ServletException  {
        int id_paziente = -1;
        Integer id_esame = -1;
        String esito;

        if (request.getParameter("id_paziente") == null || request.getParameter("id_esame") == null || request.getParameter("esito") == null)// || request.getParameter("ticket") == null)
            throw new ServletException("bad_request");
        try {
            esito = request.getParameter("esito");
            id_esame = Integer.parseInt(request.getParameter("id_esame"));
            id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException("id_utente_not_valid");
            if (id_esame <= 0) throw new NumberFormatException("id_esame_not_valid");
        } catch (NumberFormatException e) {
            throw new ServletException(e.getMessage());
        } catch (Exception e) {
            throw new ServletException();
        }

        return new Esame(-1, -1, u.getId(), esito, new Date(), id_esame, id_paziente, -1, null, null);
    }
}
