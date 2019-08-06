/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.RicettaDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.utility.Common;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
public class JDBCRicettaDao extends JDBCDao<Ricetta,Integer> implements RicettaDao{
    
    public JDBCRicettaDao(Connection con){
        super(con);
    }

    @Override
    public List<Ricetta> getRicette(Integer id_paziente) throws DaoException {
        List<Ricetta> ret = new LinkedList<>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT r.*,f.nome,p.* FROM farmaco r inner join farmaci f on f.id = r.id_farmaco inner join prescrizione p on p.id = r.id_prescrizione WHERE id_prescrizione = ?")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 Ricetta r = new Ricetta(rs.getInt("id_prescrizione"),rs.getInt("id_paziente"),rs.getInt("id_medico"),rs.getInt("id_farmaco"),rs.getString("nome"), rs.getFloat("costo"),rs.getShort("quantita"), rs.getDate("time_vendita"), rs.getDate("time"));
                 ret.add(r);
            }            
        } catch (SQLException ex) {
            throw new DaoException("Error retriving List<Ricetta>", ex);
        }        
        return ret;
    }

    @Override
    public Ricetta getByPrimaryKey(Integer id_ricetta) throws DaoException {
        Ricetta ret = null;
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT r.*,f.nome,p.* FROM farmaco r inner join farmaci f on f.id = r.id_farmaco inner join prescrizione p on p.id = r.id_prescrizione WHERE id_prescrizione = ?")) {
            stm.setInt(1, id_ricetta);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                 ret = new Ricetta(rs.getInt("p.id"),rs.getInt("id_paziente"),rs.getInt("id_medico"),rs.getInt("id_farmaco"),rs.getString("nome"), rs.getFloat("costo"),rs.getShort("quantita"), rs.getDate("time_vendita"), rs.getDate("time"));
            }            
        } catch (SQLException ex) {
            throw new DaoException("Error retriving Ricetta", ex);
        }        
        return ret;
    }

    @Override
    public Ricetta getByPrimaryKey(Integer arg0, String arg1) throws DaoException {
        throw new UnsupportedOperationException("Not supported here.");
    }

    @Override
    public boolean addRicetta(Ricetta r) throws DaoException {
        if(r == null || r.getId_prescrizione() > 0) return false;  //Se r.getId_prescrizione() > 0 allora non è una ricetta appena creata
        
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

    
}
