/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 *
 * @author Steve
 */
public class Medico extends Persona{
    private String laurea;
    private Date inizioCarriera;
    private boolean attivo;
                  
    public Medico(int id, String username, String nome, String cognome, String cf, Date data_nascita, boolean attivo, int provincia, int comune, String laurea, Date inizioCarriera, String nome_prov,String foto) {
        super(id, username, nome, cognome, cf, data_nascita, provincia, comune,"medico",nome_prov,foto);
        this.laurea = laurea;
        this.inizioCarriera = inizioCarriera;
        this.attivo = attivo;
    }

    public Medico(Persona p,String laurea, Date inizioCarriera, boolean attivo,String foto) {
        super(p.getId(), p.getUsername(), p.getNome(), p.getCognome(), p.getCf(), p.getData_nascita(), p.getProvincia(), p.getId_Comune(),"medico", p.getNome_comune(), foto);
        this.laurea = laurea;
        this.inizioCarriera = inizioCarriera;
        this.attivo = attivo;
    }

    public String getLaurea() {
        return laurea;
    }

    public Date getInizioCarriera() {
        return inizioCarriera;
    }
    
    
    public class Stats{
        public Integer count;
        public Integer mese;
        public Integer anno;

        public Stats(Integer count, Integer mese, Integer anno) {
            this.count = count;
            this.mese = mese;
            this.anno = anno;
        }
        
        
    }
}
