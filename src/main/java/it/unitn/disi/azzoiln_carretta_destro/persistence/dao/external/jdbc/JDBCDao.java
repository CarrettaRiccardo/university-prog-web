/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao.Dao;
import java.sql.Connection;
import java.util.HashMap;

/**
 * This is the base DAO class all concrete DAO using JDBC technology must extend.
 * @param <ENTITY_CLASS> the class of the entities the dao handle.
 * @param <PRIMARY_KEY_CLASS> the class of the primary key of the entity the
 * dao handle.
 * 
 * @author Steve
 */
public abstract class JDBCDao<ENTITY_CLASS, PRIMARY_KEY_CLASS> implements Dao<ENTITY_CLASS, PRIMARY_KEY_CLASS> {
    /**
     * The JDBC {@link Connection} used to access the persistence system.
     */
    protected final transient Connection CON;
    
    /**
     * The base constructor for all the JDBC DAOs.
     * @param con the internal {@code Connection}.
     */
    protected JDBCDao(Connection con) {
        super();
        this.CON = con;
    }
}
