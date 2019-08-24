/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.listeners;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDaoFactory;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Listener per il lifecycle dell' App Web
 * @author Steve
 */
public class AppListener implements ServletContextListener{
    
    
    /**
     * Al deploy dell' App apro la connessione al DB
     * @param sce 
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	String relativePath = sce.getServletContext().getInitParameter("photo_dir_name");
    	sce.getServletContext().setAttribute("PHOTOS_DIR", relativePath);
        
        
        String dburl = sce.getServletContext().getInitParameter("dburl");
        String user = sce.getServletContext().getInitParameter("username");
        String password = sce.getServletContext().getInitParameter("password");
        try {
            JDBCDaoFactory.init(dburl,user,password);
            DaoFactory daoFactory = JDBCDaoFactory.getInstance();
            sce.getServletContext().setAttribute("daoFactory", daoFactory);
            
            // Ricky hash per remember_me
            HashMap<String, Integer> mapRememberMe = new HashMap<>();// TOKEN -> ID UTENTE
            sce.getServletContext().setAttribute("hashRememberMe", mapRememberMe);
            
        } catch (DaoFactoryException ex) {
            Logger.getLogger(getClass().getName()).severe(ex.toString());
            throw new RuntimeException(ex);
        }
    }

    
    /**
     * Alla chiusura dell' applicazione chiudo la connessione al DB
     * @param sce 
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DaoFactory daoFactory = (DaoFactory) sce.getServletContext().getAttribute("daoFactory");
        if (daoFactory != null) {
            daoFactory.shutdown();
        }
        daoFactory = null;
    }
}
