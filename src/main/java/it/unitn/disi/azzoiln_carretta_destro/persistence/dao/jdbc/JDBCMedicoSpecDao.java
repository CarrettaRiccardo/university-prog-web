package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.MedicoSpecDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Steve
 */
public class JDBCMedicoSpecDao extends JDBCDao<MedicoSpecialista,Integer> implements MedicoSpecDao{

    public JDBCMedicoSpecDao(Connection con) {
        super(con);
    } 
    
    @Override
    public boolean newVisita(VisitaSpecialistica visita) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean pagaTicket(Integer id_paziente, Integer id_ticket) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Paziente getPaziente(Integer id_paziente) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean richiamaPaziente(Integer id_paziente) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Paziente> getPazienti(Integer id_medico) throws DaoException {
        if(id_medico == null || id_medico <= 0) throw new IdNotFoundException("id_medico");
        List<Paziente> ret = new LinkedList<>();
        /*DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
        LocalDateTime now = LocalDateTime.now(); */
   
        /*try (PreparedStatement stm = CON.prepareStatement(  "SELECT u.id,nome,cognome,data_nascita,path \n" +
                                                            "FROM utenti u left join foto f on f.id_utente = u.id  \n" +
                                                            "WHERE u.id IN (\n" +
                                                            "		SELECT id_paziente\n" +
                                                            "           FROM prescrizione p inner join visita_specialistica v ON p.id = v.id_prescrizione\n" +
                                                            "           WHERE time_visita = ? " +
                                                            "    )\n" +
                                                            "ORDER BY cognome,nome")) {  tengo la versione uguale a MEDICO
        stm.setString(1, now.toString().split("T")[0]);  //ritorna una striga con anche il fuso orario. Alora tengo solo la data*/
        try (PreparedStatement stm = CON.prepareStatement("SELECT u.id,nome,cognome,data_nascita,path FROM utenti u left join foto f on f.id_utente = u.id  WHERE id_medico <> ? ORDER BY cognome,nome")) {
            stm.setInt(1, id_medico); 
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 Paziente r = new Paziente(rs.getInt("id"),rs.getString("nome"), rs.getString("cognome"), rs.getDate("data_nascita"),rs.getString("path"));
                 ret.add(r);
            }            
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }
    
}
