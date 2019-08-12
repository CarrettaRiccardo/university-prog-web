package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 *
 * @author Steve
 */
public class Visita extends Prescrizione {
    private String anamnesi;

    public Visita(String anamnesi, int id, int id_paziente, int id_medico, Date time) {
        super(id, id_paziente, id_medico, time);
        this.anamnesi = anamnesi;
    }
    
    /**
     * Per creazione nuova Visita
     * @param anamnesi
     * @param id_paziente
     * @param id_medico 
     */
    public Visita(String anamnesi, int id_paziente, int id_medico) {
        super( -1, id_paziente, id_medico, new Date(System.currentTimeMillis()) );
        this.anamnesi = anamnesi;
    }

    public String getAnamnesi() {
        return anamnesi;
    }
    
    public String getAnamnesiShort() {
        return anamnesi.substring(0, 20) + "..";
    }

}
