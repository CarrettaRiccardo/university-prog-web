package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Steve
 */
public class Paziente extends Persona{
    
    private int id_medico;
    private boolean attivo;

    public Paziente(int id, String username, String nome, String cognome,Date data_nascita,String cf, int id_medico, int provincia, int comune, boolean paziente_attivo ) {
        super(id, username, nome, cognome, cf, data_nascita,  provincia, comune, "paziente", nome_provincia);
        this.id_medico = id_medico;
        this.attivo = paziente_attivo;
    }

    public Paziente(int id, String username, String nome, String cognome,Date data_nascita,String cf, int id_medico, int provincia, int comune, boolean paziente_attivo, int res) {
        super(id, username, nome, cognome, cf, data_nascita,  provincia, comune, res, "paziente", nome_provincia);
        this.id_medico = id_medico;
        this.attivo = paziente_attivo;
    }


    public int getId_medico() {
        return id_medico;
    }
    
    
    
}
