/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao.Dao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This is the JDBC implementation of {@code DAOFactory}.
 * @author Steve
 */
public class JDBCDaoFactory implements DaoFactory {
    private final transient Connection CON;
    private final transient HashMap<Class, Dao> DAO_CACHE;

    private static JDBCDaoFactory instance;
    
    /**
     * The private constructor used to create the singleton instance of this
     * {@code DAOFactory}.
     * @param dbUrl the url to access the database.
     * @throws DaoFactoryException if an error occurred during {@code DAOFactory}
     * creation.
     */
    private JDBCDaoFactory(String dbUrl,String user,String password) throws DaoFactoryException {
        super();

        /*try {
            Class.forName("com.mysql.jdbc.Driver");  
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe.getMessage(), cnfe.getCause());
        }*/

        try {
            CON = DriverManager.getConnection(dbUrl,user,password);
        } catch (SQLException sqle) {
            throw new DaoFactoryException("Cannot create connection", sqle);
        }
        
        DAO_CACHE = new HashMap<>();
    }
    
    
    /**
     * Call this method before use the instance of this class.
     * @param dbUrl the url to access to the database.
     * @param user the url to access to the database.
     * @param password the url to access to the database.
     * @throws DaoFactoryException if an error occurred during dao factory
     * configuration.
     */
    public static void init(String dbUrl,String user,String password) throws DaoFactoryException {
        if (instance == null) {
            instance = new JDBCDaoFactory(dbUrl,user,password);
        } else {
            throw new DaoFactoryException("DAOFactory already configured. You can call configure only one time");
        }
    }
    
    /**
     * Returns the singleton instance of this {@link DaoFactory}.
     * @return the singleton instance of this {@code DAOFactory}.
     * @throws DaoFactoryException if an error occurred if this dao factory is
     * not yet configured.
     */
    public static JDBCDaoFactory getInstance() throws DaoFactoryException {
        if (instance == null) {
            throw new DaoFactoryException("DAOFactory not yet configured. Call DAOFactory.configure(String dbUrl) before use the class");
        }
        return instance;
    }
    

    /**
     * Shutdowns the DB connection
     */
    @Override
    public void shutdown() {
        try {
            //DriverManager.getConnection("jdbc:derby:;shutdown=true");
            CON.close();
        } catch (SQLException sqle) {
            Logger.getLogger(JDBCDaoFactory.class.getName()).info(sqle.getMessage());
        }
    }

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
    @Override
    public <DAO_CLASS extends Dao> DAO_CLASS getDAO(Class<DAO_CLASS> daoInterface) throws DaoFactoryException {
        Dao dao = DAO_CACHE.get(daoInterface);
        if (dao != null) {
            return (DAO_CLASS) dao;
        }
        
        Package pkg = daoInterface.getPackage();
        String prefix = pkg.getName() + ".jdbc.JDBC";
        
        try {
            Class daoClass = Class.forName(prefix + daoInterface.getSimpleName());
            
            Constructor<DAO_CLASS> constructor = daoClass.getConstructor(Connection.class);
            DAO_CLASS daoInstance = constructor.newInstance(CON);
            if (!(daoInstance instanceof JDBCDao)) {
                throw new DaoFactoryException("The daoInterface passed as parameter doesn't extend JDBCDAO class");
            }
            DAO_CACHE.put(daoInterface, daoInstance);
            return daoInstance;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException ex) {
            throw new DaoFactoryException("Impossible to return the DAO", ex);
        }
    }
}
