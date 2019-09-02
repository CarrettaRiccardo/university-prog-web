package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.MedicoSpecDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ticket;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Statistiche;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Steve
 */
public class JDBCMedicoSpecDao extends JDBCDao<MedicoSpecialista, Integer> implements MedicoSpecDao {

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
        if (id_medico == null || id_medico <= 0) throw new IdNotFoundException("id_medico");
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
        try (PreparedStatement stm = CON.prepareStatement("SELECT u.id,username,nome,cognome,data_nascita,path FROM utenti u left join foto f on f.id_utente = u.id  WHERE u.id <> ? AND ruolo <> 'ssp' ORDER BY cognome,nome")) {
            stm.setInt(1, id_medico);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Paziente r = new Paziente(rs.getInt("id"), rs.getString("username"), rs.getString("nome"), rs.getString("cognome"), rs.getDate("data_nascita"), rs.getString("path"));
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
    public boolean compileVisitaSpecialistica(VisitaSpecialistica visita, Integer id_medico_spec) throws DaoException {
        if (visita == null || id_medico_spec <= 0 || visita.getId_paziente() <= 0 || visita.getId() <= 0) return false;

        try {
            Integer id_ticket = null;
            PreparedStatement ps = CON.prepareStatement("insert into ticket (costo,tipo) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setFloat(1, Ticket.costo_visite_specialistiche);
            ps.setString(2, "");

            int count = ps.executeUpdate();
            if (count == 0) return false;

            ResultSet key = ps.getGeneratedKeys();
            if (key.next()) id_ticket = key.getInt(1); //prendo l'ID del Ticket appena inserito
            else return false;

            ps = CON.prepareStatement("update visita_specialistica set anamnesi = ?, time_visita = NOW(), id_medico_specialista = ?, id_ticket = ?, cura = ? WHERE id_prescrizione = ?");
            ps.setString(1, visita.getAnamnesi());
            ps.setInt(2, id_medico_spec);
            ps.setInt(3, id_ticket);
            ps.setString(4, visita.getCura());
            ps.setInt(5, visita.getId());
            count = ps.executeUpdate();
            if (count == 0) return false;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DaoException(ex.getMessage(), ex);
        }

        return true;
    }

    /**
     * Ottiene la data di ultima visita per essere mostrata nell' elenco completo dei pazienti
     *
     * @param id_paziente
     * @return
     * @throws IdNotFoundException
     * @throws DaoException
     */
    private Date getLastDataVisita(int id_paziente) throws IdNotFoundException, DaoException {
        if (id_paziente <= 0) throw new IdNotFoundException("id_medico");
        Date ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT id, MAX(time) as data FROM visita_specialistica v inner join prescrizione p ON v.id_prescrizione = p.id WHERE id_paziente = ? ")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                ret = rs.getDate("data");
            } else {
                throw new DaoException("no_data_last_visita");
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    /**
     * Ottiene la data di ultima ricetta per essere mostrata nell' elenco completo dei pazienti
     *
     * @param id
     * @return
     */
    private Date getLastDataRicetta(int id_paziente) throws IdNotFoundException, DaoException {
        if (id_paziente <= 0) throw new IdNotFoundException("id_medico");
        Date ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT id, MAX(time) as data FROM farmaco v inner join prescrizione p ON v.id_prescrizione = p.id WHERE id_paziente = ? ")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                ret = rs.getDate("data");
            } else {
                throw new DaoException("no_data_last_ricetta");
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    
    
    
    @Override
    public ArrayList<ArrayList<Integer>> getStatsVisiteSpecialistiche(int id_medico) throws DaoException {
        if(id_medico <= 0) throw new IdNotFoundException("id_medico");
        ArrayList< ArrayList<Integer> > ret2 = new ArrayList<>();
        
        try (PreparedStatement stm = CON.prepareStatement(  "SELECT COUNT(p.id) as tot, seq.mese, seq.anno\n" +
                                                            "FROM \n" +
                                                            "     (\n" +
                                                            "      SELECT * FROM\n" +
                                                            "        (\n" +
                                                            "			SELECT 1 AS mese UNION SELECT 2  UNION SELECT 3 UNION SELECT 4 UNION\n" +
                                                            "			SELECT 5 UNION SELECT 6  UNION SELECT 7 UNION SELECT 8 UNION \n" +
                                                            "			SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12\n" +
                                                            "		) as tmp , (SELECT DISTINCT YEAR(time_visita) as anno FROM visita_specialistica WHERE id_medico_specialista = ? AND YEAR(time_visita) >= YEAR(NOW()) - 2) as tmp2\n" +
                                                            "      ) AS seq \n" +
                                                            "LEFT JOIN (prescrizione p  inner join visita_specialistica f ON f.id_medico_specialista = ? AND p.id = f.id_prescrizione) ON seq.mese = MONTH(time_visita) AND seq.anno = YEAR(time_visita)\n" +
                                                            "WHERE YEAR(time_visita) >= YEAR(NOW()) - 2 OR time IS NULL\n" +
                                                            "GROUP BY seq.anno, seq.mese\n" +
                                                            "ORDER BY seq.anno, seq.mese")) {
            stm.setInt(1, id_medico);
            stm.setInt(2, id_medico);
            ResultSet rs = stm.executeQuery();
            for (int i = 0; i < 12; i++) {
                ret2.add(new ArrayList<Integer>());
            }
            
            Statistiche m = new Statistiche();
            while (rs.next()) {
                 System.out.println(rs.getInt("tot"));
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
}
