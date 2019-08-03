package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao.Dao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Esame;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import it.unitn.disi.azzoiln_carretta_destro.utility.Common;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Steve
 */
public class JDBCUtenteDao extends JDBCDao<Utente,Integer> implements UtenteDao<Utente>{

    public JDBCUtenteDao(Connection con){
        super(con);
    }
    
    
    @Override
    public Utente getByPrimaryKey(Integer id) throws DaoException {
        Utente ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM utenti WHERE id = ?")) {
            stm.setInt(1, id);
            
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    if(rs.getString("ruolo").equals("paziente")){
                        ret = new Paziente(rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getDate("data_nascita"), rs.getString("cf"),
                                           rs.getInt("id_medico"), rs.getInt("provincia"), rs.getInt("comune"),
                                           rs.getBoolean("paziente_attivo"));
                    }
                    else{
                        ret = new Medico(rs.getString("laurea"), rs.getDate("inizio_carriera"),rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"), 
                                           rs.getBoolean("medico_attivo"), rs.getInt("provincia"), rs.getInt("comune"));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DaoException("Errore in getByPrimaryKey di UtenteDao", ex);
        }
        return ret;
    }


    /**
     * Verifica se username e password sono corretti. 
     * 
     * @param username
     * @param password
     * @return -3 errore metodo, -2 password errata, -1 username non trovato, 0 successo (utente paziente), 1 successo 
     * (scelta tra medico e paziente), 2 successo (SSR)
     * @throws it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException
     *
    */
    @Override
    public int login(String username, String password) throws DaoException {
        int ret = -3;
        String to_check = null;
        
        try {
            to_check = Common.getPasswordHash(password);
        } catch (NoSuchAlgorithmException ex) {
            throw new DaoException("Error retriving password hash NoSuchAlgorithmException UtenteDao", ex);
        } catch (InvalidKeySpecException ex) {
            throw new DaoException("Error retriving password hash InvalidKeySpecException UtenteDao", ex);
        }
        
        if(to_check == null || to_check.equals(""))//getPasswordHash ha ritornato un valore non valido
            return ret;
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT password, ruolo FROM utenti WHERE username = ?")) {
            stm.setString(1, username);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                     if(rs.getString("password").equals(to_check)){
                         switch(rs.getString("ruolo")){
                             case "paziente": ret = 0; break;
                             case "medico": 
                             case "medico_spec": ret = 1; break;
                             case "ssp": ret = 2; break;
                         }
                     }
                     else{
                         ret = -2; //username trovato, password non uguale
                     }
                }
                else{
                    ret = -1;
                }
            }
        } catch (SQLException ex) {
            throw new DaoException("Error retriving salt UtenteDao", ex);
        }
        
        return ret;
    }
    
    /**
     * Permette di modificare dati di un Paziente o di un Medico. Altre classi sollevano DaoException
     * @param user Nuovi dati da inserire
     * @return T on success
     * @throws it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException
     */
    @Override
    public boolean update(Utente user) throws DaoException{
        if(user == null) return false;
        
        try (PreparedStatement ps = getUpdateStatement(user)) {
            int count = ps.executeUpdate();
            if(count == 0) return false;
            else if (count > 1) throw new DaoException("Update affected an invalid number of records: " + count);
            else if(count == 1) return true;
            
        } catch (SQLException ex) {
            throw new DaoException("Impossible to update Utente", ex);
        }
        return false;
    }
    
    private PreparedStatement getUpdateStatement(Utente user) throws SQLException, DaoException{
        PreparedStatement ret = null;
        
        if(user instanceof Paziente){
            Paziente p = (Paziente) user;
            ret = CON.prepareStatement("UPDATE utenti SET provincia = ?, comune = ?,"
                    + "id_medico = ? WHERE id = ? AND ruolo = 'paziente' ");
            ret.setInt(1,p.getProvincia());
            ret.setInt(2,p.getComune());
            ret.setInt(3,p.getId_medico());
            ret.setInt(4,p.getId());
        }
        else if(user instanceof Medico){
            throw new DaoException("Ehi, non so cosa deve modificare. Se hai idee dimmelo :)");
        }
        else throw new DaoException("Non è possibile modificare le informazioni di un oggetto Utente");
        
        return ret;
    }
    
    
    /**
     * Ottiene l' elenco delle visite del paziente ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle visite del paziente ordinate in ordine cronologico inverso
     */
    @Override
    public List<Visita> getVisite(Integer id_paziente){
        LinkedList<Visita> ret = new LinkedList<>();
        return ret;
    }
    
    /**
     * Ottiene l' elenco delle visite specialistiche del paziente ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle visite specialistiche del paziente ordinate in ordine cronologico inverso
     */
    @Override
    public List<Visita> getVisiteSpecialistiche(Integer id_paziente){
        LinkedList<Visita> ret = new LinkedList<>();
        return ret;
    }
    
    /**
     * Ottiene l' elenco delle ricette ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle ricette ordinate in ordine cronologico inverso
     */
    @Override
    public List<Ricetta> getRicette(Integer id_paziente){
        LinkedList<Ricetta> ret = new LinkedList<>();
        return ret;
    }
    
    
    /**
     * Ottiene l' elenco degli esami del paziente ordinati in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco degli esami del paziente ordinati in ordine cronologico inverso
     */
    @Override
    public List<Esame> getEsami(Integer id_paziente){
        LinkedList<Esame> ret = new LinkedList<>();
        return ret;
    }

    
    
}