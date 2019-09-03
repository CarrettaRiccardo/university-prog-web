package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Steve
 */
public class Persona extends Utente {
    private String nome, cognome, cf, ruolo;
    private String foto; //path foto_profilo
    private char sesso;
    private Date data_nascita;

    public Persona(int id, String username, String nome, String cognome, String cf, Date data_nascita, Integer provincia, Integer comune, String ruolo, String nome_provincia, String nome_comune, String foto, char sesso) {
        super(id, username, provincia, nome_provincia, comune, nome_comune);
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.data_nascita = data_nascita;
        this.ruolo = ruolo;
        this.foto = foto;
        this.sesso = sesso;
    }

    public Persona(int id, String username, String nome, String cognome, String cf, Date data_nascita, Integer provincia, Integer comune, int res, String ruolo, String nome_provincia, String nome_comune) {
        super(id, username, provincia,nome_provincia, comune, nome_comune, res);
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.data_nascita = data_nascita;
        this.ruolo = ruolo;
        this.foto = "";
    }
    
    public Persona(int id, String username, String nome, String cognome, Date data,String ruolo,String foto) {
        super(id,username);
        this.nome = nome;
        this.cognome = cognome;
        this.data_nascita = data;
        this.ruolo = ruolo;
        this.foto = foto;
    }

    public char getSesso() {
        return sesso;
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
    
    /**
     * 
     * @return Il path originale della foto
     */
    public String getFoto() {
        Random r = new Random();
        return getUsername() + File.separator + "foto.jpg" + "?rand=" + r.nextInt(1000000);
    }
    
    public String getFotoSmall() {
        Random r = new Random();
        return getUsername() + File.separator + "foto_small.jpg" + "?rand=" + r.nextInt(1000000);
    }

    public Date getData_nascita() {
        return data_nascita;
    }

    public String getData_nascita_Stringa() {
        String date = "";
        try{
            date = getData_nascita().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);
        } catch (Exception ex){
            // se fa errore nella conversione (In caso la data sia nulla ad esempio)
        }
        return date;
    }
}
