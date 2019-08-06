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
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Persona;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ssp;
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
 * Implementazione UtenteDao con driver JDBC
 * @author Steve
 */
public class JDBCUtenteDao extends JDBCDao<Utente,Integer> implements UtenteDao{

    public JDBCUtenteDao(Connection con){
        super(con);
    }
    
    
    @Override
    public Utente getByPrimaryKey(Integer id, String s) throws DaoException {
        Utente ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT u.*, p.nome as nome_provincia FROM utenti u inner join province p on p.id = u.provincia WHERE u.id = ?")) {
            stm.setInt(1, id);
            
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    if(rs.getString("ruolo").equals("paziente") || (rs.getString("ruolo").equals("medico") && s.equals("paziente")) || (rs.getString("ruolo").equals("medico_spec") && s.equals("paziente")) ){
                        ret = new Paziente(rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getDate("data_nascita"), rs.getString("cf"),
                                           rs.getInt("id_medico"), rs.getInt("provincia"), rs.getInt("comune"),
                                           rs.getBoolean("paziente_attivo"), rs.getString("nome_provincia"));
                    }
                    else if(rs.getString("ruolo").equals("medico")){
                        ret = new Medico(rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"), 
                                           rs.getBoolean("medico_attivo"), rs.getInt("provincia"), rs.getInt("comune"), 
                                           rs.getString("laurea"), rs.getDate("inizio_carriera"), rs.getString("nome_provincia"));
                    }
                    /*
                    DA COMPLETARE
                    else if(rs.getString("ruolo").equals("medico_spec")){
                        ret = new MedicoSpecialista(rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"), 
                                           rs.getBoolean("medico_attivo"), rs.getInt("provincia"), rs.getInt("comune"), rs.getString("laurea"), rs.getDate("inizio_carriera"));
                    }
                    else if(rs.getString("ruolo").equals("ssp")){
                        ret = new Medico(rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"), 
                                           rs.getBoolean("medico_attivo"), rs.getInt("provincia"), rs.getInt("comune"), rs.getString("laurea"), rs.getDate("inizio_carriera"));
                    }*/
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
    public Utente login(String username, String password) throws DaoException {
        Utente ret = null;
        int res = -3;
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT u.*, p.nome as nome_provincia FROM utenti u inner join province p on p.id = u.provincia WHERE username = ?")) {
            stm.setString(1, username);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                     if(Common.validatePassword(password, rs.getString("password"))){
                         switch(rs.getString("ruolo")){
                             case "paziente": ret = getPaziente(rs, 0); break;
                             case "medico": ret = getPersona(rs, 1, "medico"); break;
                             case "medico_spec": ret = getPersona(rs, 1, "medico_spec"); break;  //Ottengo una Persona che indica che la scelta fra Paziente e Medico non è ancora stata fatta
                             case "ssp": ret = getSSP(rs, 2); break;
                         }
                     }
                     else{
                         ret = new Utente(-2); //username trovato, password non uguale
                     }
                }
                else{
                    ret = new Utente(-1); //username non trovato
                }
            } catch (NoSuchAlgorithmException ex) {
                throw new DaoException("NoSuchAlgorithmException UtenteDao", ex);
            } catch (InvalidKeySpecException ex) {
                throw new DaoException("InvalidKeySpecException UtenteDao", ex);
            }
        } catch (SQLException ex) {
            throw new DaoException("Error retriving salt UtenteDao", ex);
        }
        
        return ret;
    }
    
    private Ssp getSSP(ResultSet rs, int res) throws SQLException{
        return new Ssp(rs.getInt("id"),rs.getString("username"), rs.getInt("provincia"),rs.getString("nome_provincia"),res);           
    }
    
    /**
     * 
     * @param rs
     * @param res
     * @return Oggetto Persona con i dati conuni a tutte le persone. In aggiunta il campo ruolo per distinguere tra medico, medico_spec
     * @throws SQLException 
     */
    private Persona getPersona(ResultSet rs, int res,String ruolo) throws SQLException{
        return new Persona(rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"),
                                           rs.getInt("provincia"), rs.getInt("comune"), res,ruolo,rs.getString("nome_provincia"));        
    }
    
    private Paziente getPaziente(ResultSet rs, int res) throws SQLException{
        return new Paziente(rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getDate("data_nascita"), rs.getString("cf"),
                                           rs.getInt("id_medico"), rs.getInt("provincia"), rs.getInt("comune"),
                                           rs.getBoolean("paziente_attivo"), res,rs.getString("nome_provincia"));        
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
            ret.setInt(2,p.getId_Comune());
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
    public List<Ricetta> getRicette(Integer id_paziente) throws DaoException {
        List<Ricetta> ret = new LinkedList<>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT r.*,f.nome,p.* FROM farmaco r inner join farmaci f on f.id = r.id_farmaco inner join prescrizione p on p.id = r.id_prescrizione WHERE id_prescrizione = ? ORDER BY time DESC")) {
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

    @Override
    public Utente getByPrimaryKey(Integer arg0) throws DaoException {
        throw new UnsupportedOperationException("Not supported here."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
