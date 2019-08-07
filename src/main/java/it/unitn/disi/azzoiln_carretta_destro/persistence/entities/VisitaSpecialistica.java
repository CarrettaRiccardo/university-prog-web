package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

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

    public Date getTime_visita() {
        return time_visita;
    }

    public String getNome_visita() {
        return nome_visita;
    }

    public String getNome_medico_specialista() {
        return nome_medico_specialista;
    }
    
    
}
