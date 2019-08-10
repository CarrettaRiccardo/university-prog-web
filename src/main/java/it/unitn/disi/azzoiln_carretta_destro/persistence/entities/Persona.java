package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 * @author Steve
 */
public class Persona extends Utente {
    private String nome, cognome, cf, ruolo, nome_comune;
    private String foto; //path foto_profilo
    private Date data_nascita;
    private int id_comune;

    public Persona(int id, String username, String nome, String cognome, String cf, Date data_nascita, int provincia, int comune, String ruolo, String nome_provincia,String foto) {
        super(id, username, provincia, nome_provincia);
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.data_nascita = data_nascita;
        this.ruolo = ruolo;
        this.nome_comune = nome_comune;
        this.id_comune = id_comune;
        this.foto = foto;
    }

    public Persona(int id, String username, String nome, String cognome, String cf, Date data_nascita, int provincia, int comune, int res, String ruolo, String nome_provincia) {
        super(id, username, provincia,nome_provincia, res);
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.data_nascita = data_nascita;
        this.ruolo = ruolo;
        this.foto = "";
    }
    
    public Persona(int id, String nome, String cognome, Date data,String ruolo,String foto) {
        super(id,"");
        this.nome = nome;
        this.cognome = cognome;
        this.data_nascita = data;
        this.ruolo = ruolo;
        this.foto = foto;
    }


    public int getId_Comune() {
        return id_comune;
    }

    public String getNome_comune() {
        return nome_comune;
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
     * @return Il path originale della foto oppure "images/defaul.jpeg" se trova Stringa vuota | null
     */
    public String getFoto() {
        return (! (foto == null || foto.equals(""))) ? foto : "assets/uploads/profilo/default.jpg" ;
    }

    public Date getData_nascita() {
        return data_nascita;
    }


}
