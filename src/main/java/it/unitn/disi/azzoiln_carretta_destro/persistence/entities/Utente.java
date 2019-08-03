package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 *
 * @author Steve
 */
public class Utente {
    private int id;
    private String username;
    private String nome,cognome, cf;
    private Date data_nascita;
    private boolean attivo;
    private int provincia, comune;

    public Utente(int id, String username, String nome, String cognome, String cf, Date data_nascita, boolean attivo, int provincia, int comune) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.attivo = attivo;
        this.data_nascita = data_nascita;
    }

    public int getProvincia() {
        return provincia;
    }

    public int getComune() {
        return comune;
    }

    public int getId() {
        return id;
    }
    
    public boolean isAttivo() {
        return attivo;
    }

    public String getUsername() {
        return username;
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
