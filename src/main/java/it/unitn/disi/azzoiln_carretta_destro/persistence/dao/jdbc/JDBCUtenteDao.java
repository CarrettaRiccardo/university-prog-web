package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;


import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao.Dao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Esame;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Farmaci;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Persona;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ssp;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.VisiteSpecialistiche;
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
    
    private JDBCMedicoDao medico;
    private JDBCPazienteDao paziente;
    private JDBCMedicoSpecDao medicoSpec;
    private JDBCSspDao ssp;

    public JDBCUtenteDao(Connection con){
        super(con);
        medico = new JDBCMedicoDao(con);
        paziente = new JDBCPazienteDao(con);
        medicoSpec = new JDBCMedicoSpecDao(con);
        ssp = new JDBCSspDao(con);
    }
    
    
    /**
     * Da accesso ai metodi dell' interfaccia MedicoDao
     * @return 
     */
    @Override
    public JDBCMedicoDao Medico(){
        return medico;
    }    
    /**
     * Da accesso ai metodi dell' interfaccia pazienteDao
     * @return 
     */
    @Override
    public JDBCPazienteDao Paziente() {
        return paziente;
    }
    /**
     * Da accesso ai metodi dell' interfaccia MedicoSpecDao
     * @return 
     */
    @Override
    public JDBCMedicoSpecDao MedicoSpecialista() {
        return medicoSpec;
    }
    /**
     * Da accesso ai metodi dell' interfaccia SspDao
     * @return 
     */
    @Override
    public JDBCSspDao Ssp() {
        return ssp;
    }
    
    
    @Override
    public Utente getByPrimaryKey(Integer id, String s) throws DaoException {
        Utente ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT u.*, p.nome as nome_provincia,path FROM utenti u inner join province p on p.id = u.provincia left join foto f on u.id = f.id_utente WHERE u.id = ?")) {
            stm.setInt(1, id);
            
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    if(rs.getString("ruolo").equals("paziente") || (rs.getString("ruolo").equals("medico") && s.equals("paziente")) || (rs.getString("ruolo").equals("medico_spec") && s.equals("paziente")) ){
                        ret = new Paziente(rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getDate("data_nascita"), rs.getString("cf"),
                                           rs.getInt("id_medico"), rs.getInt("provincia"), rs.getInt("comune"),
                                           rs.getBoolean("paziente_attivo"), rs.getString("nome_provincia"), rs.getString("path"));
                    }
                    else if(rs.getString("ruolo").equals("medico")){
                        ret = new Medico(rs.getInt("id"),rs.getString("username"), rs.getString("nome"),
                                           rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"), 
                                           rs.getBoolean("medico_attivo"), rs.getInt("provincia"), rs.getInt("comune"), 
                                           rs.getString("laurea"), rs.getDate("inizio_carriera"), rs.getString("nome_provincia"),rs.getString("path"));
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
            throw new DaoException("db_error", ex);
        }
        return ret;
    }
    
    
     /**
     * Il valore di ritorno -2 non è qui usato
     * Il valore di ritorno -4 indica 'id non trovato'
     * @param id
     * @return
     * @throws DaoException 
     */
    @Override
    public Utente getByPrimaryKey(Integer id) throws DaoException {
        Utente ret = null;
        int res = -3;
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT u.*, p.nome as nome_provincia FROM utenti u inner join province p on p.id = u.provincia WHERE id = ?")) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    switch(rs.getString("ruolo")){
                        case "paziente": ret = getPaziente(rs, 0); break;
                        case "medico": ret = getPersona(rs, 1, "medico"); break;
                        case "medico_spec": ret = getPersona(rs, 1, "medico_spec"); break;  //Ottengo una Persona che indica che la scelta fra Paziente e Medico non è ancora stata fatta
                        case "ssp": ret = getSSP(rs, 2); break;
                    }                     
                }
                else{
                    ret = new Utente(-4); //id non trovato
                }
            } catch (SQLException ex) {
                throw new DaoException("db_error", ex);
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        
        return ret;
    }
    
    
    


    /**
     * Verifica se username e password sono corretti. 
     * Se ritorna un SSP la modalità di visualizzazione è SSP
     * Se ritorna Paziente | Medico | Medico.spec. la modalità sarà rispettivamente Paziente | Medico | Medico_spec
     * Se ritorna un oggetto Persona la modalità non è decisa, bisogna farla scegliere (ChooseSevlet)
     * @param username
     * @param password
     * @return Un Utente con res = -3 errore metodo, -2 password errata, -1 username non trovato, 0 successo (utente paziente), 1 successo 
               (scelta tra medico e paziente), 2 successo (SSR)
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
                throw new DaoException("no_alghorithm", ex);
            } catch (InvalidKeySpecException ex) {
                throw new DaoException("invalid_key", ex);
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
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
            throw new DaoException("db_error", ex);
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
        else throw new DaoException("update_error");
        
        return ret;
    }
    
    
    /**
     * Ottiene l' elenco delle visite del paziente ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle visite del paziente ordinate in ordine cronologico inverso
     */
    @Override
    public List<Visita> getVisite(Integer id_paziente) throws DaoException{
        if(id_paziente == null || id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        LinkedList<Visita> ret = new LinkedList<>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT v.*,p.* FROM visita v inner join prescrizione p on p.id = v.id_prescrizione WHERE p.id_paziente = ? ORDER BY time DESC")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 Visita r = new Visita(rs.getString("anamnesi"),rs.getInt("id"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getDate("time"));
                 ret.add(r);
            }            
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }  
        return ret;
    }
    
    /**
     * Ottiene l' elenco delle visite specialistiche del paziente ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle visite specialistiche del paziente ordinate in ordine cronologico inverso
     * @throws it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException
     */
    @Override
    public List<VisitaSpecialistica> getVisiteSpecialistiche(Integer id_paziente) throws DaoException{
        if(id_paziente == null || id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        LinkedList<VisitaSpecialistica> ret = new LinkedList<>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT v.*,p.*,v2.nome as nome_visita,'not_yet' as nome_medico_spec FROM visita_specialistica v inner join prescrizione p on p.id = v.id_prescrizione inner join visite_specialistiche v2 on v2.id = v.id_visita_spec WHERE p.id_paziente = ? ORDER BY time DESC")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 VisitaSpecialistica r = new VisitaSpecialistica(rs.getInt("id_medico_specialista"), rs.getInt("id_ticket"), rs.getInt("id"), rs.getInt("id_visita_spec"), rs.getString("anamnesi"), rs.getDate("time_visita"), rs.getString("nome_visita"), rs.getString("nome_medico_spec"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getDate("time"));
                 ret.add(r);
            }            
        } catch (SQLException ex) {
            //throw new DaoException("db_error", ex);
            System.out.println(ex.getMessage() + "\n\n");
        }
        return ret;
    }
    
    /**
     * Ottiene l' elenco delle ricette ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle ricette ordinate in ordine cronologico inverso
     */
    @Override
    public List<Ricetta> getRicette(Integer id_paziente) throws DaoException {
        if(id_paziente == null || id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        List<Ricetta> ret = new LinkedList<>();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT r.*,f.nome,p.* FROM farmaco r inner join farmaci f on f.id = r.id_farmaco inner join prescrizione p on p.id = r.id_prescrizione WHERE id_paziente = ? ORDER BY time DESC")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 Ricetta r = new Ricetta(rs.getInt("id_prescrizione"),rs.getInt("id_paziente"),rs.getInt("id_medico"),rs.getInt("id_farmaco"),rs.getString("nome"), rs.getFloat("costo"),rs.getShort("quantita"), rs.getDate("time_vendita"), rs.getDate("time"));
                 ret.add(r);
            }            
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }
    
    
    /**
     * Ottiene l' elenco degli esami del paziente ordinati in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco degli esami del paziente ordinati in ordine cronologico inverso
     */
    @Override
    public List<Esame> getEsami(Integer id_paziente) throws DaoException{
        if(id_paziente == null || id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        LinkedList<Esame> ret = new LinkedList<>();
        return ret;
    }   

    @Override
    public Farmaci getFarmaci() throws DaoException {
        Farmaci ret = new Farmaci();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT id,nome FROM farmaci ORDER BY nome")) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 ret.addFarmaco(rs.getInt("id"),rs.getString("nome"));
                 //ret.add(r);
            }            
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }
    
    @Override
    public Farmaci getFarmaci(String hint) throws DaoException {
        Farmaci ret = new Farmaci();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT id,nome FROM farmaci WHERE nome LIKE ? ORDER BY nome")) {
            stm.setString(1, "%" + hint + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 ret.addFarmaco(rs.getInt("id"),rs.getString("nome"));
                 //ret.add(r);
            }            
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }
    
    
    @Override
    public VisiteSpecialistiche getAllVisiteSpec(String hint) throws DaoException {
        VisiteSpecialistiche ret = new VisiteSpecialistiche();
        
        try (PreparedStatement stm = CON.prepareStatement("SELECT id,nome FROM visite_specialistiche WHERE nome LIKE ? ORDER BY nome")) {
            stm.setString(1, "%" + hint + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                 ret.addVisitaSpecialistica(rs.getInt("id"),rs.getString("nome"));
                 //ret.add(r);
            }            
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }        
        return ret;
    }
}
