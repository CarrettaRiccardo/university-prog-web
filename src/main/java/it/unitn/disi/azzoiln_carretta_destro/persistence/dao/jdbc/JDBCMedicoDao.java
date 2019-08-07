package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.MedicoDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
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
    public boolean addVisita(Integer id_medico, Integer id_paziente, Visita visita) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addVisita(Visita visita) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addRicetta(Ricetta r) throws DaoException {
        if(r == null || r.getId() > 0) return false;  //Se r.getId_prescrizione() > 0 allora non è una ricetta appena creata
        
        try {
            PreparedStatement ps = CON.prepareStatement("insert into prescrizione (id_paziente,id_medico) VALUES (?,?)");
            ps.setInt(1, r.getId_paziente());
            ps.setInt(2, r.getId_medico());
            
            int count = ps.executeUpdate();
            if(count == 0) return false;
            
            ps = CON.prepareStatement("SELECT id FROM prescrizione WHERE id_paziente = ? AND id_medico= ? ORDER BY id DESC LIMIT 1");  //questo perchè MYSQL non supporta la sintassi RETURNING
            ps.setInt(1, r.getId_paziente());
            ps.setInt(2, r.getId_medico());
            
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
    public boolean addVisitaSpecialistica(Integer id_medico, Integer id_paziente, Integer id_visita_specialistica) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addEsame(Integer id_medico, Integer id_paziente, Integer id_esame) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Paziente> getPazienti(Integer id_medico) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
