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
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Date;
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
        if(visita == null || visita.getId_medico() <= 0 || visita.getId_paziente() <= 0) return false;  
        
        try {
            Integer new_id = null;
            PreparedStatement ps = CON.prepareStatement("insert into prescrizione (id_paziente,id_medico) VALUES (?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, visita.getId_paziente());
            ps.setInt(2, visita.getId_medico());
            
            int count = ps.executeUpdate();
            if(count == 0) return false;
            
            ResultSet key = ps.getGeneratedKeys();
            
            if(key.next()) new_id = key.getInt(1); //prendo l'ID dell'ultima prescrizione fatta
            else return false;
            
            ps = CON.prepareStatement("insert into visita (id_prescrizione,anamnesi) VALUES (?,?)");
            ps.setInt(1, new_id);
            ps.setString(2, visita.getAnamnesi());
            count = ps.executeUpdate();
            if(count == 0) return false;
        } 
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DaoException(ex.getMessage(), ex);
        }
        
        return true;
    }

    @Override
    public boolean addRicetta(Ricetta r) throws DaoException {
        if(r == null || r.getId() > 0) return false;  //Se r.getId_prescrizione() > 0 allora non è una ricetta appena creata
        
        try {
            Integer new_id = null;
            PreparedStatement ps = CON.prepareStatement("insert into prescrizione (id_paziente,id_medico) VALUES (?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, r.getId_paziente());
            ps.setInt(2, r.getId_medico());
            System.out.println("qua");
            int count = ps.executeUpdate();
            if(count == 0) return false;
            System.out.println("qua2");
            ResultSet key = ps.getGeneratedKeys();
            if(key.next()) new_id = key.getInt(1); //prendo l'ID dell'ultima prescrizione fatta
            else return false;
            System.out.println("qua2");
            
            
            ps = CON.prepareStatement("insert into farmaco (id_prescrizione, id_farmaco, quantita) VALUES (?,?,?)");
            ps.setInt(1, new_id);
            ps.setInt(2, r.getId_farmaco());
            ps.setInt(3, r.getQuantita());
            count = ps.executeUpdate();
            
            if(count == 0) return false;
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        
        return true;
    }

    @Override
    public boolean addVisitaSpecialistica(VisitaSpecialistica v) throws DaoException {
        if(v.getId_medico() <= 0 || v.getId_paziente() <= 0 || v.getId_visita_spec() <= 0) return false;  
        
        try {
            Integer new_id = null;
            PreparedStatement ps = CON.prepareStatement("insert into prescrizione (id_paziente,id_medico) VALUES (?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, v.getId_paziente());
            ps.setInt(2, v.getId_medico());
            
            int count = ps.executeUpdate();
            if(count == 0) return false;
            ResultSet key = ps.getGeneratedKeys();
            if(key.next()) new_id = key.getInt(1); //prendo l'ID dell'ultima prescrizione fatta
            else return false;
            
            ps = CON.prepareStatement("insert into visita_specialistica (id_prescrizione,id_visita_spec) VALUES (?,?)");
            ps.setInt(1, new_id);
            ps.setInt(2, v.getId_visita_spec());
            count = ps.executeUpdate();
            if(count == 0) return false;
        } catch (SQLException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
        
        return true;
    }

    @Override
    public boolean addEsame(Esame e) throws DaoException {
        if(e.getId_medico() <= 0 || e.getId_paziente() <= 0 || e.getId_esame() <= 0) return false;  
        
        try {
            Integer new_id = null;
            PreparedStatement ps = CON.prepareStatement("insert into prescrizione (id_paziente,id_medico) VALUES (?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, e.getId_paziente());
            ps.setInt(2, e.getId_medico());
            
            int count = ps.executeUpdate();
            if(count == 0) return false;
            ResultSet key = ps.getGeneratedKeys();
            if(key.next()) new_id = key.getInt(1); //prendo l'ID dell'ultima prescrizione fatta
            else return false;
            
            ps = CON.prepareStatement("insert into esame (id_prescrizione,id_esame) VALUES (?,?)");
            ps.setInt(1, new_id);
            ps.setInt(2, e.getId_esame());
            count = ps.executeUpdate();
            if(count == 0) return false;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DaoException("add_error", ex);
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
            while (rs.next()) {
                 System.out.println(rs.getInt("id"));
                 Paziente r = new Paziente(rs.getInt("id"),rs.getString("nome"), rs.getString("cognome"), rs.getDate("data_nascita"),rs.getString("path"));
                 r.setLastVisita(getLastDataVisita(r.getId()));
                 r.setLastRicetta(getLastDataRicetta(r.getId()));
                 ret.add(r);
            }            
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }

    @Override
    public boolean isMyPatient(Integer id_paziente, Integer id_medico) throws DaoException {
        boolean ret = false;
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT id FROM utenti u WHERE id=? AND id_medico = ?")) {
            stm.setInt(1, id_paziente);
            stm.setInt(2, id_medico);
            ResultSet rs = stm.executeQuery();
            if (rs.next()){
                 System.out.println("Si, " + id_paziente + " è mio " + id_medico);
                 ret = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n\n\n");
            throw new DaoException("db_error", ex);
        } 
        return ret;
    }
    
    
    /**
     * Ottiene la data di ultima visita per essere mostrata nell' elenco completo dei pazienti
     * @param id_paziente
     * @return
     * @throws IdNotFoundException
     * @throws DaoException 
     */
    private Date getLastDataVisita(int id_paziente) throws IdNotFoundException, DaoException {
        if(id_paziente <= 0) throw new IdNotFoundException("id_medico");
        Date ret = null;
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT id, MAX(time_visita) as data FROM visita_specialistica v inner join prescrizione p ON v.id_prescrizione = p.id WHERE id_paziente = ? ")) {
            stm.setInt(1, id_paziente); 
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                ret = rs.getDate("data");
            }        
            else{
                throw new DaoException("no_data_last_visita");
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }

    /**
     * Ottiene la data di ultima ricetta per essere mostrata nell' elenco completo dei pazienti
     * @param id
     * @return 
     */
    private Date getLastDataRicetta(int id_paziente) throws IdNotFoundException, DaoException {
        if(id_paziente <= 0) throw new IdNotFoundException("id_medico");
        Date ret = null;
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT id, MAX(time_vendita) as data FROM farmaco v inner join prescrizione p ON v.id_prescrizione = p.id WHERE id_paziente = ? ")) {
            stm.setInt(1, id_paziente); 
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                ret = rs.getDate("data");
            }        
            else{
                throw new DaoException("no_data_last_ricetta");
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }
    
    
}
