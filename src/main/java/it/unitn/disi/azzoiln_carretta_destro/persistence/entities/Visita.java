package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 *
 * @author Steve
 */
public class Visita extends Prescrizione {
    private int id_ticket;
    private String anamnesi;
    private Date time_visita;

    public Visita(int id_ticket, String anamnesi, Date time_visita, int id, int id_paziente, int id_medico, Date time) {
        super(id, id_paziente, id_medico, time);
        this.id_ticket = id_ticket;
        this.anamnesi = anamnesi;
        this.time_visita = time_visita;
    }

    public int getId_ticket() {
        return id_ticket;
    }

    public String getAnamnesi() {
        return anamnesi;
    }
    
    public String getAnamnesiShort() {
        return anamnesi.substring(0, 10);
    }

    public Date getTime_visita() {
        return time_visita;
    }
}
