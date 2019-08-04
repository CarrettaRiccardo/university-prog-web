package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 *
 * @author Steve
 */
public class Persona extends Utente{
    private String nome,cognome, cf;
    private Date data_nascita;
    private boolean attivo;
    private int comune;
    
    public Persona(int id, String username, String nome, String cognome, String cf, Date data_nascita, boolean attivo, int provincia, int comune) {
        super(id,username, provincia);
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.attivo = attivo;
        this.data_nascita = data_nascita;
    }
    
    public Persona(int id, String username, String nome, String cognome, String cf, Date data_nascita, boolean attivo, int provincia, int comune, int res) {
        super(id,username, provincia,res);
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.attivo = attivo;
        this.data_nascita = data_nascita;
    }


    public int getComune() {
        return comune;
    }
    
    public boolean isAttivo() {
        return attivo;
    }    

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCf() {
        return cf;
    }

    public Date getData_nascita() {
        return data_nascita;
    }

    
}
