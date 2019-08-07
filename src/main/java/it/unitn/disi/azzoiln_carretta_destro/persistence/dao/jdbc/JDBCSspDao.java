/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.SspDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ssp;
import java.sql.Connection;

/**
 *
 * @author Steve
 */
public class JDBCSspDao extends JDBCDao<Ssp,Integer> implements SspDao{
    
    public JDBCSspDao(Connection con) {
        super(con);
    } 

    @Override
    public boolean erogaEsame(Integer id_esame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
