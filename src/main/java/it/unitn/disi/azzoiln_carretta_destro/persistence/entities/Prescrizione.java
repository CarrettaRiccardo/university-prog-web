package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 *
 * @author Steve
 */
public class Prescrizione {
    private int id, id_paziente, id_medico;
    private Date time;

    public Prescrizione(int id, int id_paziente, int id_medico, Date time) {
        this.id = id;
        this.id_paziente = id_paziente;
        this.id_medico = id_medico;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getId_paziente() {
        return id_paziente;
    }

    public int getId_medico() {
        return id_medico;
    }

    public Date getTime() {
        return time;
    }
    
    
}
