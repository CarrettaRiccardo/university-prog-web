package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Steve
 */
public class Paziente extends Persona{
    
    private Integer id_medico;
    private boolean attivo;
    private Date ultima_visita, ultima_ricetta;

    
    public Paziente(int id, String username, String nome, String cognome,Date data_nascita,String cf, Integer id_medico, Integer provincia, Integer comune, boolean paziente_attivo,String nome_provincia,String nome_comune, String foto,char sesso) {
        super(id, username, nome, cognome, cf, data_nascita,  provincia, comune, "paziente", nome_provincia, nome_comune,foto,sesso);
        this.id_medico = id_medico;
        this.attivo = paziente_attivo;
    }

    
    /**
     * Per oggetto recuperato durante il login.Va aggiunto il campo res per individuare errori al login
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
     * @param nome_comune 
     */
    public Paziente(int id, String username, String nome, String cognome,Date data_nascita,String cf, Integer id_medico, Integer provincia, Integer comune, boolean paziente_attivo, int res,String nome_provincia, String nome_comune) {
        super(id, username, nome, cognome, cf, data_nascita,  provincia, comune, res, "paziente", nome_provincia, nome_comune);
        this.id_medico = id_medico;
        this.attivo = paziente_attivo;
    }

    /**
     * Per oggetto recuperato per essere mostrato nell'elenco dei pazienti di un medico
     */
    public Paziente(int id, String username, String nome, String cognome, Date data,String foto) {
        super(id,username,nome,cognome,data,"paziente",foto);
    }


    public Integer getId_medico() {
        return id_medico;
    }
    
    public void setLastVisita(Date data){
        this.ultima_visita = data;
    }
    
    public void setLastRicetta(Date data){
        this.ultima_ricetta = data;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public Date getLastVisita() {
        return ultima_visita;
    }

    public Date getLastRicetta() {
        return ultima_ricetta;
    }
    
    
}
