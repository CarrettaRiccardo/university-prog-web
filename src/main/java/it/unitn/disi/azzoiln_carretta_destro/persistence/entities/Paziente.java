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

    public Paziente(int id, String username, String nome, String cognome,Date data_nascita,String cf, int id_medico, int provincia, int comune, boolean paziente_attivo,String nome_provincia ) {
        super(id, username, nome, cognome, cf, data_nascita,  provincia, comune, "paziente", nome_provincia);
        this.id_medico = id_medico;
        this.attivo = paziente_attivo;
    }

    
    /**
     * Per oggetto recuperato durante il login. Va aggiunto il campo res per individuare errori al login
     * @param id
     * @param username
     * @param nome
     * @param cognome
     * @param data_nascita
     * @param cf
     * @param id_medico
     * @param provincia
     * @param comune
     * @param paziente_attivo
     * @param res
     * @param nome_provincia 
     */
    public Paziente(int id, String username, String nome, String cognome,Date data_nascita,String cf, int id_medico, int provincia, int comune, boolean paziente_attivo, int res,String nome_provincia) {
        super(id, username, nome, cognome, cf, data_nascita,  provincia, comune, res, "paziente", nome_provincia);
        this.id_medico = id_medico;
        this.attivo = paziente_attivo;
    }


    public int getId_medico() {
        return id_medico;
    }
    
    
    
}
