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
     * [Per lettura dati Utente] Il comportamento è speculare a login, ma legge i dati partendo dalla PK. Per leggere i dati dopo
     * aver preso id_utente dal REMEMBER ME. Per maggiori info vedi login(..) in JDBCUtenteDao
     * @param primaryKey
     * @return Oggetto che rappresenta l'ogetto che vogliamo ottenere
     * @throws DaoException 
     */
    public ENTITY_CLASS getByPrimaryKey(PRIMARY_KEY_CLASS primaryKey) throws DaoException;
}
