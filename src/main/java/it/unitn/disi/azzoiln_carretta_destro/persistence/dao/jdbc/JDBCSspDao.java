/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.SspDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ssp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

    @Override
    public List<Medico> getMedici(Integer id_provincia) throws DaoException {
        List<Medico> ret = new LinkedList<>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT *, p.nome as nome_provincia FROM utenti u inner join province p on p.id = u.provincia left join foto f on u.id = f.id_utente  WHERE ruolo = 'medico' ORDER BY u.cognome,u.nome")) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(getMedico(rs));
            }            
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }
    
    private Medico getMedico(ResultSet rs) throws SQLException{
        return new Medico(rs.getInt("id"), rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"), rs.getBoolean("medico_attivo"),
                                           rs.getInt("provincia"), rs.getInt("comune"), rs.getString("laurea"), rs.getDate("inizio_carriera"), rs.getString("nome_provincia"), rs.getString("path"));        
    }
    
    private Paziente getPaziente(ResultSet rs) throws SQLException{
        return new Paziente(rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getDate("data_nascita"), rs.getString("cf"),
                                           rs.getInt("id_medico"), rs.getInt("provincia"), rs.getInt("comune"),
                                           rs.getBoolean("paziente_attivo"), rs.getString("nome_provincia"), rs.getString("path"));   
    }

    @Override
    public List<Paziente> getPazienti(Integer id_provincia) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getNomeProvincia(Integer id_provincia) throws DaoException {
        String ret = "";
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT nome FROM province as p WHERE p.id = ?")) {
            stm.setInt(1, id_provincia);
            ResultSet rs = stm.executeQuery();
            if (rs.next()){
                ret = rs.getString("nome");
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }
    
    // Serve perchè non esiste la classe Provincia, e le tuple normali non esistono
    @Override
    public Integer getIdProvincia(String nome_provincia) throws DaoException {
        Integer ret = null;
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT id FROM province as p WHERE p.nome = ?")) {
            stm.setString(1, nome_provincia);
            ResultSet rs = stm.executeQuery();
            if (rs.next()){
                ret = rs.getInt("id");
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }

    @Override
    public List<String> getListProvince() throws DaoException {
        List<String> ret = new LinkedList<>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT nome FROM province")) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                ret.add(rs.getString("nome"));
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }
}
