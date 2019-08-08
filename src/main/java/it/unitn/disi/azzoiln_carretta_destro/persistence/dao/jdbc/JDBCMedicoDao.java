package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.MedicoDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Esame;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Siccome la classe sarà accessibile solamente tramite JDBCUtenteDao è inutile reimplementare i metodi di UtenteDao anche qui
 * (cosa che verrebbe resa necessaria se MedicoDao estendesse UtenteDao). Quindi MedicoDao non estende UtenteDao.
 * @author Steve
 */
class JDBCMedicoDao extends JDBCDao<Medico,Integer> implements MedicoDao{
    
    public JDBCMedicoDao(Connection con) {
        super(con);
    }    

    
    @Override
    public boolean addVisita(Visita visita) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addRicetta(Ricetta r) throws DaoException {
        if(r == null || r.getId() > 0) return false;  //Se r.getId_prescrizione() > 0 allora non è una ricetta appena creata
        
        try {
            Integer new_id = null;
            PreparedStatement ps = CON.prepareStatement("insert into prescrizione (id_paziente,id_medico) VALUES (?,?)");
            ps.setInt(1, r.getId_paziente());
            ps.setInt(2, r.getId_medico());
            
            int count = ps.executeUpdate();
            if(count == 0) return false;
            
            ResultSet key = ps.getGeneratedKeys();
            if(key.next()) new_id = key.getInt(0); //prendo l'ID dell'ultima prescrizione fatta
            else return false;
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id_new_ricetta = rs.getInt("id");
                ps = CON.prepareStatement("insert into farmaco (id_prescrizione, id_farmaco, quantita) VALUES (?,?,?)");
                ps.setInt(1, id_new_ricetta);
                ps.setInt(2, r.getId_farmaco());
                ps.setInt(3, r.getQuantita());
                count = ps.executeUpdate();
                if(count == 0) return false;
            }
            else return false;
        } catch (SQLException ex) {
            throw new DaoException("Impossible to create Ricetta", ex);
        }
        
        return true;
    }

    @Override
    public boolean addVisitaSpecialistica(Integer id_medico, Integer id_paziente, Integer id_visita_specialistica) throws DaoException {
        if(id_medico == null || id_medico == null || id_visita_specialistica == null) return false;  
        
        try {
            Integer new_id = null;
            PreparedStatement ps = CON.prepareStatement("insert into prescrizione (id_paziente,id_medico) VALUES (?,?)");
            ps.setInt(1, id_paziente);
            ps.setInt(2, id_medico);
            
            int count = ps.executeUpdate();
            if(count == 0) return false;
            ResultSet key = ps.getGeneratedKeys();
            if(key.next()) new_id = key.getInt(0); //prendo l'ID dell'ultima prescrizione fatta
            else return false;
            
            ps = CON.prepareStatement("insert into visita_specialistica (id_prescrizione) VALUES (?)");
            ps.setInt(1, new_id);
            count = ps.executeUpdate();
            if(count == 0) return false;
        } catch (SQLException ex) {
            throw new DaoException("Impossible to create VisitaSpec", ex);
        }
        
        return true;
    }

    @Override
    public boolean addEsame(Integer id_medico, Integer id_paziente, Integer id_esame) throws DaoException {
        if(id_medico == null || id_medico == null || id_esame == null) return false;  
        
        try {
            Integer new_id = null;
            PreparedStatement ps = CON.prepareStatement("insert into prescrizione (id_paziente,id_medico) VALUES (?,?)");
            ps.setInt(1, id_paziente);
            ps.setInt(2, id_medico);
            
            int count = ps.executeUpdate();
            if(count == 0) return false;
            ResultSet key = ps.getGeneratedKeys();
            if(key.next()) new_id = key.getInt(0); //prendo l'ID dell'ultima prescrizione fatta
            else return false;
            
            ps = CON.prepareStatement("insert into esame (id_prescrizione,id_esame) VALUES (?,?)");
            ps.setInt(1, new_id);
            ps.setInt(1, id_esame);
            count = ps.executeUpdate();
            if(count == 0) return false;
        } catch (SQLException ex) {
            throw new DaoException("Impossible to create Esame", ex);
        }
        
        return true;
    }

    @Override
    public List<Paziente> getPazienti(Integer id_medico) throws DaoException {
        if(id_medico == null || id_medico <= 0) throw new IdNotFoundException("id_medico");
        List<Paziente> ret = new LinkedList<>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT u.id,nome,cognome,data_nascita,path FROM utenti u left join foto f on f.id_utente = u.id  WHERE id_medico = ? ORDER BY cognome,nome")) {
            stm.setInt(1, id_medico);
            ResultSet rs = stm.executeQuery();
            System.out.println("id_medico");
            while (rs.next()) {
                System.out.println(rs.getInt("id"));
                 Paziente r = new Paziente(rs.getInt("id"),rs.getString("nome"), rs.getString("cognome"), rs.getDate("data_nascita"),rs.getString("path"));
                 ret.add(r);
            }            
        } catch (SQLException ex) {
            throw new DaoException("Error retriving List<Paziente>", ex);
        }        
        return ret;
    }
    
    
}
