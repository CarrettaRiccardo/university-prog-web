/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao.Dao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;

/**
 * This interface must be implemented by all the concrete {@code DAOFactor(y)}ies.
 * @author Steve
 */
public interface DaoFactory {
    
    /**
     * Shutdowns the connection to the storage system.
     */
    public void shutdown();
    
    
    /**
     * Returns the concrete {@link Dao dao} which type is the class passed as
     * parameter.
     * 
     * @param <DAO_CLASS> the class name of the {@code dao} to get.
     * @param daoInterface the class instance of the {@code dao} to get.
     * @return the concrete {@code dao} which type is the class passed as
     * parameter.
     * @throws DaoFactoryException if an error occurred during the operation.
     */
    public <DAO_CLASS extends Dao> DAO_CLASS getDAO(Class<DAO_CLASS> daoInterface) throws DaoFactoryException;
}
