package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 * @author Steve
 */
public class Persona extends Utente {
    private String nome, cognome, cf, ruolo;
    private Date data_nascita;
    private int comune;

    public Persona(int id, String username, String nome, String cognome, String cf, Date data_nascita, int provincia, int comune, String ruolo) {
        super(id, username, provincia);
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.data_nascita = data_nascita;
        this.ruolo = ruolo;
    }

    public Persona(int id, String username, String nome, String cognome, String cf, Date data_nascita, int provincia, int comune, int res, String ruolo) {
        super(id, username, provincia, res);
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.data_nascita = data_nascita;
        this.ruolo = ruolo;
    }


    public int getComune() {
        return comune;
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

    public String getRuolo() {
        return ruolo;
    }

    public Date getData_nascita() {
        return data_nascita;
    }


}
