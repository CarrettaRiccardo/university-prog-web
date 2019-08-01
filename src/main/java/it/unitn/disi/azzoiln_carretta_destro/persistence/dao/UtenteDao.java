/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao.Dao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Esame;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import java.util.List;

/**
 * Interfaccia della classe UtenteDaoJDBC contenente i metodi che l' implementazione dovrà implementare
 * @author Steve
 */
public interface UtenteDao<Utente> extends Dao<Utente, Integer>{

    
    
    /**
     * Verifica se username e password sono corretti. 
     * 
     * @return -1 per fallimento, 0 successo (utente paziente), 1 successo 
     * (scelta tra utente e paziente), 2 successo (SSR)
     *
    */
    public int login(String username, String password);
    
    /**
     * 
     * @param user Nuovi dati da inserire
     * @return T on success
     */
    public boolean update(Utente user);
    
    
    /**
     * Ottiene l' elenco delle visite del paziente ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle visite del paziente ordinate in ordine cronologico inverso
     */
    public List<Visita> getVisite(Integer id_paziente);
    
    /**
     * Ottiene l' elenco delle visite specialistiche del paziente ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle visite specialistiche del paziente ordinate in ordine cronologico inverso
     */
    public List<Visita> getVisiteSpecialistiche(Integer id_paziente);
    
    /**
     * Ottiene l' elenco delle ricette ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle ricette ordinate in ordine cronologico inverso
     */
    public List<Visita> getRicette(Integer id_paziente);
    
    
    /**
     * Ottiene l' elenco degli esami del paziente ordinati in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco degli esami del paziente ordinati in ordine cronologico inverso
     */
    public List<Esame> getEsami(Integer id_paziente);
}
