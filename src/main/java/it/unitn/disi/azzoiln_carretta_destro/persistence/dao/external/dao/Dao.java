/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;

/**
 * The basic DAO interface that all DAOs must implement.
 * @author Steve
 */
public interface Dao<ENTITY_CLASS, PRIMARY_KEY_CLASS> {
    
    /**
     * 
     * @param primaryKey
     * @param modalita "Forza" il tipo di ritorno. Ovvero se un utente è un medico e modalità = 'paziente', allora ottengo come risultato un oggetto di tipo Paziente
     * @return Oggetto che rappresenta l'ogetto che vogliamo ottenere
     * @throws DaoException 
     */
    public ENTITY_CLASS getByPrimaryKey(PRIMARY_KEY_CLASS primaryKey, String modalita) throws DaoException;
    
    /**
     * 
     * @param primaryKey
     * @return Oggetto che rappresenta l'ogetto che vogliamo ottenere
     * @throws DaoException 
     */
    public ENTITY_CLASS getByPrimaryKey(PRIMARY_KEY_CLASS primaryKey) throws DaoException;
}
