package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Steve
 */
public class Paziente extends Utente{
    
    private int id_medico;

    public Paziente(int id, String username, String nome, String cognome,Date data_nascita,String cf, int id_medico, int provincia, int comune, boolean paziente_attivo ) {
        super(id, username, nome, cognome, cf, data_nascita, paziente_attivo, provincia, comune);
        this.id_medico = id_medico;
    }


    public int getId_medico() {
        return id_medico;
    }
    
    
    
}
