package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.PazienteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Prenotazione;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ticket;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Statistiche;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
                "(`id_paziente`,`id_medico`,time) VALUES " +
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
        if (idMedico != null){
            try (PreparedStatement stm = CON.prepareStatement("SELECT p.id_paziente as paz, p.id_medico as med, p.time as data, u.nome as pazNome, u.cognome as pazCognome, u2.nome as medNome, u2.cognome as medCognome FROM prenotazione as p inner JOIN utenti as u on p.id_paziente = u.id inner join utenti as u2 on p.id_medico = u2.id"
                    + " WHERE p.time >= (\"" + data + " 00:00:00\")"
                    + " AND p.time <= (\"" + data + " 23:59:59\")"
                    + " AND p.id_medico = " + idMedico)) {
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                     ret.add(new Prenotazione(rs.getInt("paz"), rs.getInt("med"), rs.getString("data")));//, rs.getString("pazNome").concat(" ").concat(rs.getString("pazCognome")), rs.getString("medNome").concat(" ").concat(rs.getString("medCognome"))));
                }
            } catch (SQLException ex) {
                throw new DaoException("db_error", ex);
            }        
        }
        return ret;
    }
    
    @Override
    public List<Prenotazione> getPrenotazioni(Integer id_paziente) throws DaoException  {
        List<Prenotazione> ret = new LinkedList<Prenotazione>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT id_paziente, id_medico, time FROM prenotazione"
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

    @Override
    public Boolean setDataVisitaSpecialistica(Integer id_visita, String data) throws DaoException {
        Boolean success = false;
        try {
            PreparedStatement ps = CON.prepareStatement("UPDATE visita_specialistica SET time_visita = ? WHERE id_prescrizione = ?");
            ps.setString(1, data);
            ps.setInt(2, id_visita);
            success = ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return success;
    }
    
    @Override
    public Boolean setDataEsame(Integer id_esame, String data) throws DaoException {
        Boolean success = false;
        try {
            PreparedStatement ps = CON.prepareStatement("UPDATE esame SET time_esame = ? WHERE id_prescrizione = ?");
            ps.setString(1, data);
            ps.setInt(2, id_esame);
            success = ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return success;
    }
    
    @Override
    public ArrayList< ArrayList<Integer> > getStatsRicette(int id_paziente)throws DaoException {
        if(id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        ArrayList< ArrayList<Integer> > ret2 = new ArrayList<>();
        
        try (PreparedStatement stm = CON.prepareStatement(  "SELECT COUNT(p.id) as tot, seq.mese, seq.anno\n" +
                                                            "FROM \n" +
                                                            "     (\n" +
                                                            "      SELECT * FROM\n" +
                                                            "        (\n" +
                                                            "			SELECT 1 AS mese UNION SELECT 2  UNION SELECT 3 UNION SELECT 4 UNION\n" +
                                                            "			SELECT 5 UNION SELECT 6  UNION SELECT 7 UNION SELECT 8 UNION \n" +
                                                            "			SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12\n" +
                                                            "		) as tmp , (SELECT DISTINCT YEAR(time) as anno FROM prescrizione WHERE id_paziente = ? AND YEAR(time) >= YEAR(NOW()) - 2) as tmp2\n" +
                                                            "      ) AS seq \n" +
                                                            "LEFT JOIN (prescrizione p  inner join farmaco f ON p.id_paziente = ? AND p.id = f.id_prescrizione)  ON seq.mese = MONTH(p.time) AND seq.anno = YEAR(p.time)\n" +
                                                            "WHERE YEAR(time) >= YEAR(NOW()) - 2 OR time IS NULL\n" +
                                                            "GROUP BY seq.anno, seq.mese\n" +
                                                            "ORDER BY seq.anno, seq.mese")) {
            stm.setInt(1, id_paziente);
            stm.setInt(2, id_paziente);
            ResultSet rs = stm.executeQuery();
            for (int i = 0; i < 12; i++) {
                ret2.add(new ArrayList<Integer>());
            }
            
            while (rs.next()) {
                 Statistiche m = new Statistiche();
                 Statistiche.LightStats s = m.new LightStats(rs.getInt("tot"), rs.getInt("mese"), rs.getInt("anno"));
                 ret2.get(s.mese-1).add(s.count);                    
            }            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DaoException("db_error", ex);
        }    
        
        if(ret2.get(0).size() == 0){ //se non c'è nessun elemento meto degli 0 per evitare problemi nel forEach in JSP
            for (int i = 0; i < 12; i++) {
                ret2.get(i).add(0);
            }
        }
        
        
        int i = 0;
        System.out.println("STAMPO STATS_RICETTE");
        for(ArrayList<Integer> m : ret2){
            System.out.print("Mese:" + i);
            for(Integer m2 : m){
                System.out.print(" -> " + m2);
            }
            System.out.println("");
            i++;
        }
        return ret2;
    }

    @Override
    public ArrayList<ArrayList<Integer>> getStatsVisite(int id_paziente) throws DaoException {
        if(id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        ArrayList< ArrayList<Integer> > ret2 = new ArrayList<>();
        
        try (PreparedStatement stm = CON.prepareStatement(  "SELECT COUNT(p.id) as tot, seq.mese, seq.anno\n" +
                                                            "FROM \n" +
                                                            "     (\n" +
                                                            "      SELECT * FROM\n" +
                                                            "        (\n" +
                                                            "			SELECT 1 AS mese UNION SELECT 2  UNION SELECT 3 UNION SELECT 4 UNION\n" +
                                                            "			SELECT 5 UNION SELECT 6  UNION SELECT 7 UNION SELECT 8 UNION \n" +
                                                            "			SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12\n" +
                                                            "		) as tmp , (SELECT DISTINCT YEAR(time) as anno FROM prescrizione WHERE id_paziente = ? AND YEAR(time) >= YEAR(NOW()) - 2) as tmp2\n" +
                                                            "      ) AS seq \n" +
                                                            "LEFT JOIN (prescrizione p  inner join visita f ON p.id_paziente = ? AND p.id = f.id_prescrizione)  ON seq.mese = MONTH(p.time) AND seq.anno = YEAR(p.time)\n" +
                                                            "WHERE YEAR(time) >= YEAR(NOW()) - 2 OR time IS NULL\n" +
                                                            "GROUP BY seq.anno, seq.mese\n" +
                                                            "ORDER BY seq.anno, seq.mese")) {
            stm.setInt(1, id_paziente);
            stm.setInt(2, id_paziente);
            ResultSet rs = stm.executeQuery();
            for (int i = 0; i < 12; i++) {
                ret2.add(new ArrayList<Integer>());
            }
            
            while (rs.next()) {
                 System.out.println(rs.getInt("tot"));
                 Statistiche m = new Statistiche();
                 Statistiche.LightStats s = m.new LightStats(rs.getInt("tot"), rs.getInt("mese"), rs.getInt("anno"));
                 ret2.get(s.mese-1).add(s.count);                    
            }            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DaoException("db_error", ex);
        }   
        
        if(ret2.get(0).size() == 0){ //se non c'è nessun elemento meto degli 0 per evitare problemi nel forEach in JSP
            for (int i = 0; i < 12; i++) {
                ret2.get(i).add(0);
            }
        }
        
        return ret2;
    }

    @Override
    public ArrayList<ArrayList<Integer>> getStatsVisiteSpecialistiche(int id_paziente) throws DaoException {
        if(id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        ArrayList< ArrayList<Integer> > ret2 = new ArrayList<>();
        
        try (PreparedStatement stm = CON.prepareStatement(  "SELECT COUNT(p.id) as tot, seq.mese, seq.anno\n" +
                                                            "FROM \n" +
                                                            "     (\n" +
                                                            "      SELECT * FROM\n" +
                                                            "        (\n" +
                                                            "			SELECT 1 AS mese UNION SELECT 2  UNION SELECT 3 UNION SELECT 4 UNION\n" +
                                                            "			SELECT 5 UNION SELECT 6  UNION SELECT 7 UNION SELECT 8 UNION \n" +
                                                            "			SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12\n" +
                                                            "		) as tmp , (SELECT DISTINCT YEAR(time) as anno FROM prescrizione WHERE id_paziente = ? AND YEAR(time) >= YEAR(NOW()) - 2) as tmp2\n" +
                                                            "      ) AS seq \n" +
                                                            "LEFT JOIN (prescrizione p  inner join visita_specialistica f ON p.id_paziente = ? AND p.id = f.id_prescrizione) ON seq.mese = MONTH(p.time) AND seq.anno = YEAR(p.time)\n" +
                                                            "WHERE YEAR(time) >= YEAR(NOW()) - 2 OR time IS NULL\n" +
                                                            "GROUP BY seq.anno, seq.mese\n" +
                                                            "ORDER BY seq.anno, seq.mese")) {
            stm.setInt(1, id_paziente);
            stm.setInt(2, id_paziente);
            ResultSet rs = stm.executeQuery();
            for (int i = 0; i < 12; i++) {
                ret2.add(new ArrayList<Integer>());
            }
            
            while (rs.next()) {
                 System.out.println(rs.getInt("tot"));
                 Statistiche m = new Statistiche();
                 Statistiche.LightStats s = m.new LightStats(rs.getInt("tot"), rs.getInt("mese"), rs.getInt("anno"));
                 ret2.get(s.mese-1).add(s.count);                    
            }            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DaoException("db_error", ex);
        }  
        
        if(ret2.get(0).size() == 0){ //se non c'è nessun elemento meto degli 0 per evitare problemi nel forEach in JSP
            for (int i = 0; i < 12; i++) {
                ret2.get(i).add(0);
            }
        }
        
        System.out.println("STAMPO STATS_VS");
        int i = 0;
        for(ArrayList<Integer> m : ret2){
            System.out.print("Mese:" + i);
            for(Integer m2 : m){
                System.out.print(" -> " + m2);
            }
            System.out.println("");
            i++;
        }
        return ret2;
    }

    @Override
    public ArrayList<ArrayList<Integer>> getStatsEsami(int id_paziente) throws DaoException {
        if(id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        ArrayList< ArrayList<Integer> > ret2 = new ArrayList<>();
        
        try (PreparedStatement stm = CON.prepareStatement(  "SELECT COUNT(p.id) as tot, seq.mese, seq.anno\n" +
                                                            "FROM \n" +
                                                            "     (\n" +
                                                            "      SELECT * FROM\n" +
                                                            "        (\n" +
                                                            "			SELECT 1 AS mese UNION SELECT 2  UNION SELECT 3 UNION SELECT 4 UNION\n" +
                                                            "			SELECT 5 UNION SELECT 6  UNION SELECT 7 UNION SELECT 8 UNION \n" +
                                                            "			SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12\n" +
                                                            "		) as tmp , (SELECT DISTINCT YEAR(time) as anno FROM prescrizione WHERE id_paziente = ? AND YEAR(time) >= YEAR(NOW()) - 2) as tmp2\n" +
                                                            "      ) AS seq \n" +
                                                            "LEFT JOIN (prescrizione p  inner join esame f ON p.id_paziente = ? AND p.id = f.id_prescrizione) ON seq.mese = MONTH(p.time) AND seq.anno = YEAR(p.time)\n" +
                                                            "WHERE YEAR(time) >= YEAR(NOW()) - 2 OR time IS NULL\n" +
                                                            "GROUP BY seq.anno, seq.mese\n" +
                                                            "ORDER BY seq.anno, seq.mese")) {
            stm.setInt(1, id_paziente);
            stm.setInt(2, id_paziente);
            ResultSet rs = stm.executeQuery();
            for (int i = 0; i < 12; i++) {
                ret2.add(new ArrayList<Integer>());
            }
            
            while (rs.next()) {
                 System.out.println(rs.getInt("tot"));
                 Statistiche m = new Statistiche();
                 Statistiche.LightStats s = m.new LightStats(rs.getInt("tot"), rs.getInt("mese"), rs.getInt("anno"));
                 ret2.get(s.mese-1).add(s.count);                    
            }            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DaoException("db_error", ex);
        }    
        
        if(ret2.get(0).size() == 0){ //se non c'è nessun elemento meto degli 0 per evitare problemi nel forEach in JSP
            for (int i = 0; i < 12; i++) {
                ret2.get(i).add(0);
            }
        }
        
        System.out.println("STAMPO STATS_ESAMI");
        int i = 0;
        for(ArrayList<Integer> m : ret2){
            System.out.print("Mese:" + i);
            for(Integer m2 : m){
                System.out.print(" -> " + m2);
            }
            System.out.println("");
            i++;
        }
        return ret2;
    }
    
    @Override
    public ArrayList<Statistiche.LightStats> getStatsPrenotazioni(int id_medico) throws DaoException {
        if(id_medico <= 0) throw new IdNotFoundException("id_medico");
        ArrayList<Statistiche.LightStats> ret2 = new ArrayList<>();
        
        try (PreparedStatement stm = CON.prepareStatement(  "SELECT CAST(time as DATE) as time, COUNT(*) as tot\n" +
                                                            "FROM prenotazione\n" +
                                                            "WHERE id_medico = ? AND YEAR(time) = YEAR(NOW()) \n" +
                                                            "GROUP BY CAST(time as DATE)\n" +
                                                            "ORDER BY CAST(time as DATE)")) {
            stm.setInt(1, id_medico);
            ResultSet rs = stm.executeQuery();
                        
            while (rs.next()) {
                 Statistiche m = new Statistiche();
                 Statistiche.LightStats s = m.new LightStats(rs.getInt("tot"), rs.getDate("time"));
                 ret2.add(s);   
            }            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DaoException("db_error", ex);
        }
        return ret2;
    }
}
