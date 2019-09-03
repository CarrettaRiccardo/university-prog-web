/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.SspDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ssp;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Statistiche;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Steve
 */
public class JDBCSspDao extends JDBCDao<Ssp, Integer> implements SspDao {

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

        try (PreparedStatement stm = CON.prepareStatement("SELECT *, p.nome as nome_provincia, c.nome as nome_comune FROM utenti u inner join province p on p.id = u.provincia  inner join comuni c on c.id = u.comune left join foto f on u.id = f.id_utente  WHERE ruolo = 'medico' AND provincia = ? ORDER BY u.cognome,u.nome")) {
            stm.setInt(1, id_provincia);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(getMedico(rs));
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public List<MedicoSpecialista> getMediciSpecialisti(Integer id_provincia) throws DaoException {
        List<MedicoSpecialista> ret = new LinkedList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT *, p.nome as nome_provincia, c.nome as nome_comune FROM utenti u inner join province p on p.id = u.provincia  inner join comuni c on c.id = u.comune left join foto f on u.id = f.id_utente  WHERE ruolo = 'medico_spec' ORDER BY u.cognome,u.nome")) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(getMedicoSpecialista(rs));
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    private Medico getMedico(ResultSet rs) throws SQLException {
        return new Medico(rs.getInt("id"), rs.getString("username"), rs.getString("nome"),
                rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"), rs.getBoolean("medico_attivo"),
                rs.getInt("provincia"), rs.getInt("comune"), rs.getString("laurea"), rs.getDate("inizio_carriera"), rs.getString("nome_provincia"), rs.getString("nome_comune"), rs.getString("path"),rs.getString("sesso").charAt(0));
    }

    private MedicoSpecialista getMedicoSpecialista(ResultSet rs) throws SQLException {
        return new MedicoSpecialista(rs.getInt("id"), rs.getString("username"), rs.getString("nome"),
                rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"), rs.getBoolean("medico_attivo"),
                rs.getInt("provincia"), rs.getInt("comune"), rs.getString("laurea"), rs.getDate("inizio_carriera"),
                rs.getString("nome_provincia"), rs.getString("nome_comune"), rs.getString("path"), rs.getString("specialita"),rs.getString("sesso").charAt(0));
    }

    private Paziente getPaziente(ResultSet rs) throws SQLException {
        return new Paziente(rs.getInt("id"), rs.getString("username"), rs.getString("nome"),
                rs.getString("cognome"), rs.getDate("data_nascita"), rs.getString("cf"),
                rs.getInt("id_medico"), rs.getInt("provincia"), rs.getInt("comune"),
                rs.getBoolean("paziente_attivo"), rs.getString("nome_provincia"), rs.getString("nome_comune"), rs.getString("path"),rs.getString("sesso").charAt(0));
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
            if (rs.next()) {
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
            if (rs.next()) {
                ret = rs.getInt("id");
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }
    
    @Override
    public String getNomeComune(Integer id_comune) throws DaoException {
        String ret = "";

        try (PreparedStatement stm = CON.prepareStatement("SELECT nome FROM comuni as c WHERE c.id = ?")) {
            stm.setInt(1, id_comune);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                ret = rs.getString("nome");
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public Integer getIdComune(String nome_comune) throws DaoException {
        Integer ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT id FROM comuni as c WHERE c.nome = ?")) {
            stm.setString(1, nome_comune);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
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

        try (PreparedStatement stm = CON.prepareStatement("SELECT nome FROM province ORDER BY nome")) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(rs.getString("nome"));
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }
    
    @Override
    public List<String> getListComuni(Integer id_prov) throws DaoException {
        List<String> ret = new LinkedList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT nome FROM comuni WHERE id_provincia = ? ORDER BY nome")) {
            stm.setInt(1, id_prov);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.add(rs.getString("nome"));
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public ArrayList<ArrayList<Integer>> getStatsEsami(int id_ssp) throws DaoException {
        if(id_ssp <= 0) throw new IdNotFoundException("id_ssp");
        ArrayList< ArrayList<Integer> > ret2 = new ArrayList<>();
        
        try (PreparedStatement stm = CON.prepareStatement(  "SELECT COUNT(p.id) as tot, seq.mese, seq.anno\n" +
                                                            "FROM \n" +
                                                            "     (\n" +
                                                            "      SELECT * FROM\n" +
                                                            "        (\n" +
                                                            "			SELECT 1 AS mese UNION SELECT 2  UNION SELECT 3 UNION SELECT 4 UNION\n" +
                                                            "			SELECT 5 UNION SELECT 6  UNION SELECT 7 UNION SELECT 8 UNION \n" +
                                                            "			SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12\n" +
                                                            "		) as tmp , (SELECT DISTINCT YEAR(time_visita) as anno FROM esame WHERE id_ssp = ? AND YEAR(time_visita) >= YEAR(NOW()) - 2) as tmp2\n" +
                                                            "      ) AS seq \n" +
                                                            "LEFT JOIN (prescrizione p  inner join esame f ON f.id_ssp = ? AND p.id = f.id_prescrizione) ON seq.mese = MONTH(time_visita) AND seq.anno = YEAR(time_visita)\n" +
                                                            "WHERE YEAR(time_visita) >= YEAR(NOW()) - 2 OR time IS NULL\n" +
                                                            "GROUP BY seq.anno, seq.mese\n" +
                                                            "ORDER BY seq.anno, seq.mese")) {
            stm.setInt(1, id_ssp);
            stm.setInt(2, id_ssp);
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
        
        System.out.println("STAMPO STATS_ESAME");
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
