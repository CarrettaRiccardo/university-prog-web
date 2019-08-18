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
    public boolean newPrenotazione(Prenotazione prenotazione) throws DaoException {
        try (PreparedStatement stm = CON.prepareStatement("INSERT INTO `prog_web`.`prenotazione` " +
                "(`id_paziente`,`id_medico`,`data`) VALUES " +
                "("+ prenotazione.getIdPaziente() + "," + prenotazione.getIdMedico() + ",'" + prenotazione.getTimestamp() + "')")) {
            stm.execute();
            return true;
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
    }

    @Override
    public List<Prenotazione> getPrenotazioni(String data, Integer idMedico) throws DaoException {
        List<Prenotazione> ret = new LinkedList<Prenotazione>();
        
        /*"SELECT p.id_paziente as paz, p.id_medico as med, p.data as data, u.nome as pazNome, u.cognome as pazCognome, u2.nome as medNome, u2.cognome as medCognome FROM prenotazione as p inner JOIN utenti as u on p.id_paziente = u.id inner join utenti as u2 on p.id_medico = u2.id"
                + " WHERE p.data >= (\"" + data + " 00:00:00\")"
                + " AND p.data <= (\"" + data + " 23:59:59\")"))*/
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT p.id_paziente as paz, p.id_medico as med, p.data as data, u.nome as pazNome, u.cognome as pazCognome, u2.nome as medNome, u2.cognome as medCognome FROM prenotazione as p inner JOIN utenti as u on p.id_paziente = u.id inner join utenti as u2 on p.id_medico = u2.id"
                + " WHERE p.data >= (\"" + data + " 00:00:00\")"
                + " AND p.data <= (\"" + data + " 23:59:59\")"
                + " AND p.id_medico = " + idMedico)) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 ret.add(new Prenotazione(rs.getInt("paz"), rs.getInt("med"), rs.getString("data")));//, rs.getString("pazNome").concat(" ").concat(rs.getString("pazCognome")), rs.getString("medNome").concat(" ").concat(rs.getString("medCognome"))));
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
                + " WHERE id_paziente = ?")) {
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
