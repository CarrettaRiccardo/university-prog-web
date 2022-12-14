package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Steve
 */
public class VisitaSpecialistica extends Prescrizione{
    private int id_medico_specialista;
    private Integer id_ticket;
    private int id_visita_spec;  //riferimento alla tabella contenete tutte le possibili visite specialistiche assegnabili
    private String anamnesi, cura;
    private Date time_visita;
    private String nome_visita, nome_medico_specialista;

    public VisitaSpecialistica(int id_medico_specialista, Integer id_ticket, int id, int id_visita_spec, String anamnesi, Date time_visita, String nome_visita, String nome_medico_specialista, int id_paziente, int id_medico, Date time,String cura) {
        super(id, id_paziente, id_medico, time);
        this.id_medico_specialista = id_medico_specialista;
        this.id_ticket = id_ticket;
        this.id_visita_spec = id_visita_spec;
        this.anamnesi = anamnesi;
        this.cura = cura;
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

    public Integer getId_ticket() {
        return id_ticket;
    }

    public int getId_visita_spec() {
        return id_visita_spec;
    }

    public String getAnamnesi() {
        return anamnesi != null ? anamnesi : "";
    }
    
    public String getAnamnesiShort() {
        if(anamnesi == null)
            return "";
        if(anamnesi.length() <= 20)
            return anamnesi;
        else 
            return anamnesi.substring(0, 20) + "..";
    }
    
    public String getCura() {
        return cura;
    }

    public Date getTime_visita() {
        return time_visita;
    }

    public String getNome_visita() {
        return nome_visita;
    }
    
    public void setNome_visita(String nome) {
        this.nome_visita = nome;
    }

    public String getNome_medico_specialista() {
        return nome_medico_specialista;
    }
    
    /**
     * 
     * @return T se la visita_spec non ?? ancora stata completata
     */
    public boolean isNew(){
        return id_medico_specialista <= 0 || id_ticket <= 0;
    }
    
    /**
     * 
     * @return T se la data della visita non ?? ancor a stata fissata
     */
    public boolean isDaFissare(){
        return time_visita == null;
    }
    
    
    /**
     * Esegue tutta la logica di gestione parametri e controllo valori non validi per la creazione di una visita da parte del medico
     * @param req 
     * @param u Utente di sessione per prelevare id_medico
     * @return  Oggetto VisitaSpecialistica
     * @throws javax.servlet.ServletException 
     */
    public static VisitaSpecialistica loadFromHttpRequestNew(final HttpServletRequest request, final Utente u) throws ServletException{
        int id_paziente = -1;
        Integer id_visita = -1;

        if (request.getParameter("id_paziente") == null || request.getParameter("id_visita") == null)
            throw new ServletException("bad_request");
        try {
            id_visita = Integer.parseInt(request.getParameter("id_visita"));
            id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException("id_utente_not_valid");
            if (id_visita <= 0) throw new NumberFormatException("id_visita_not_valid");
        } catch (NumberFormatException e) {
            throw new ServletException(e.getMessage());
        } catch (Exception e) {
            throw new ServletException();
        }
        
        return new VisitaSpecialistica(id_visita,id_paziente,u.getId());
    }
    
    
    /**
     * Esegue tutta la logica di gestione parametri e controllo valori non validi per la compilazione della visita da parte del medico_spec
     * @param req 
     * @param u Utente di sessione per prelevare id_medico
     * @return  Oggetto VisitaSpecialistica
     * @throws javax.servlet.ServletException 
     */
    public static VisitaSpecialistica loadFromHttpRequestCompila(final HttpServletRequest request, final Utente u) throws ServletException{
        int id_paziente = -1;
        Integer id_visita = -1;
        String anamnesi = "", cura = "";

        if (request.getParameter("id_paziente") == null || request.getParameter("id_visita") == null || request.getParameter("anamnesi") == null || request.getParameter("cura") == null)// || request.getParameter("ticket") == null)
            throw new ServletException("bad_request");
        try {
            id_visita = Integer.parseInt(request.getParameter("id_visita"));
            id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            anamnesi = request.getParameter("anamnesi");
            cura = request.getParameter("cura");
            if (id_paziente <= 0) throw new NumberFormatException("id_utente_not_valid");
            if (id_visita <= 0) throw new NumberFormatException("id_visita_not_valid");
        } catch (NumberFormatException e) {
            throw new ServletException(e.getMessage());
        } catch (Exception e) {
            throw new ServletException();
        }
        
        return new VisitaSpecialistica(u.getId(), -1,id_visita, -1,anamnesi, null, "", "", id_paziente,-1, null, cura);  //TODO: Costruttore apposito per questa situazione
    }
    
    
}
