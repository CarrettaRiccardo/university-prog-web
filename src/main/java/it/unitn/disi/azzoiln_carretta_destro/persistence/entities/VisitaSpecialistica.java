package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Steve
 */
public class VisitaSpecialistica extends Prescrizione{
    private int id_medico_specialista, id_ticket;
    private int id_visita_spec;  //riferimento alla tabella contenete tutte le possibili visite specialistiche assegnabili
    private String anamnesi;
    private Date time_visita;
    private String nome_visita, nome_medico_specialista;

    public VisitaSpecialistica(int id_medico_specialista, int id_ticket, int id, int id_visita_spec, String anamnesi, Date time_visita, String nome_visita, String nome_medico_specialista, int id_paziente, int id_medico, Date time) {
        super(id, id_paziente, id_medico, time);
        this.id_medico_specialista = id_medico_specialista;
        this.id_ticket = id_ticket;
        this.id_visita_spec = id_visita_spec;
        this.anamnesi = anamnesi;
        this.time_visita = time_visita;
        this.nome_visita = nome_visita;
        this.nome_medico_specialista = nome_medico_specialista;
    }

    /**
     * Costruttore per nuove Visite Specialistiche
     * @param id_visita_spec
     * @param id
     * @param id_paziente
     * @param id_medico
     * @param time 
     */
    public VisitaSpecialistica(int id_visita_spec, int id_paziente, int id_medico) {
        super(-1, id_paziente, id_medico, null);
        this.id_visita_spec = id_visita_spec;
    }
    
    
 
    
    
    

    public int getId_medico_specialista() {
        return id_medico_specialista;
    }

    public int getId_ticket() {
        return id_ticket;
    }

    public int getId_visita_spec() {
        return id_visita_spec;
    }

    public String getAnamnesi() {
        return anamnesi;
    }
    
    public String getAnamnesiShort() {
        return (anamnesi == null) ? "" : (anamnesi.substring(0, 20) + "..");
    }

    public Date getTime_visita() {
        return time_visita;
    }

    public String getNome_visita() {
        return nome_visita;
    }

    public String getNome_medico_specialista() {
        return nome_medico_specialista;
    }
    
    
    /**
     * Esegue tutta la logica di gestione parametri e controllo valori non validi
     * @param req 
     * @param u Utente di sessione per prelevare id_medico
     * @return  Oggetto VisitaSpecialistica
     * @throws javax.servlet.ServletException 
     */
    public static VisitaSpecialistica loadFromHttpRequest(final HttpServletRequest request, final Utente u) throws ServletException{
        int id_paziente = -1;
        Integer id_visita = -1;

        if (request.getParameter("id_paziente") == null || request.getParameter("id_visita") == null)
            throw new ServletException("bad_request");
        try {
            id_visita = Integer.parseInt(request.getParameter("id_visita"));
            id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException("id_paziente_not_valid");
            if (id_visita <= 0) throw new NumberFormatException("id_visita_not_valid");
        } catch (NumberFormatException e) {
            throw new ServletException(e.getMessage());
        } catch (Exception e) {
            throw new ServletException();
        }
        
        return new VisitaSpecialistica(id_visita,id_paziente,u.getId());
    }
    
    
}
