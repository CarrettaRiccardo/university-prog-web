/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

/**
 * @author Steve
 */
public interface UtenteDao {
    
    /**
     * Verifica se username e password sono corretti. 
     * 
     * @return -1 per fallimento, 0 successo (utente paziente), 1 successo 
     * (scelta tra utente e paziente), 2 successo (SSR)
     *
    */
    public int login(String username, String password);
}
