/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.PazienteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Prenotazione;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ticket;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Steve
 */
class JDBCPazienteDao extends JDBCDao<Paziente,Integer> implements PazienteDao{
    
    public JDBCPazienteDao(Connection con) {
        super(con);
    } 

    @Override
    public boolean setMedico(Integer id_paziente, Integer new_id_medico) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Medico getMedico(Integer id_medico) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean newPrenotazione(Prenotazione prenotazione) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Prenotazione> getPrenotazioni(String data) throws DaoException {
        List<Prenotazione> ret = new LinkedList<Prenotazione>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT id_paziente, id_medico, data FROM prenotazione"
                + "WHERE time_on >= (\"" + data + " 00:00:00\")"
                + " AND time_on <= (\"" + data + " 23:59:59\")")) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 ret.add(new Prenotazione(rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getString("data")));
            }            
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }
    
    @Override
    public List<Prenotazione> getPrenotazioni(Integer id_paziente) throws DaoException  {
        List<Prenotazione> ret = new LinkedList<Prenotazione>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT id_paziente, id_medico, data FROM prenotazione"
                + "WHERE id_paziente = ?")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 ret.add(new Prenotazione(rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getString("data")));
            }            
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }

    @Override
    public List<Ticket> getTickets(Integer id_paziente) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
