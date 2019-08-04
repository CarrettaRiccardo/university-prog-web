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
                  
    public Medico(int id, String username, String nome, String cognome, String cf, Date data_nascita, boolean attivo, int provincia, int comune, String laurea, Date inizioCarriera ) {
        super(id, username, nome, cognome, cf, data_nascita, provincia, comune,"medico");
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
    
    
}
