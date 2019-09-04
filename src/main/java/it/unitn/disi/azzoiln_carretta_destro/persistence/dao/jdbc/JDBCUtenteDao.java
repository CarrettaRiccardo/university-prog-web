package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.jdbc;


import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.IdNotFoundException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.jdbc.JDBCDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.*;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Esami;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Farmaci;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.VisiteSpecialistiche;
import it.unitn.disi.azzoiln_carretta_destro.utility.Common;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementazione UtenteDao con driver JDBC
 *
 * @author Steve
 */
public class JDBCUtenteDao extends JDBCDao<Utente, Integer> implements UtenteDao {

    private final JDBCMedicoDao medico;
    private final JDBCPazienteDao paziente;
    private final JDBCMedicoSpecDao medicoSpec;
    private final JDBCSspDao ssp;

    public JDBCUtenteDao(Connection con) {
        super(con);
        medico = new JDBCMedicoDao(con);
        paziente = new JDBCPazienteDao(con);
        medicoSpec = new JDBCMedicoSpecDao(con);
        ssp = new JDBCSspDao(con);
    }


    /**
     * Da accesso ai metodi dell' interfaccia MedicoDao
     *
     * @return
     */
    @Override
    public JDBCMedicoDao Medico() {
        return medico;
    }

    /**
     * Da accesso ai metodi dell' interfaccia pazienteDao
     *
     * @return
     */
    @Override
    public JDBCPazienteDao Paziente() {
        return paziente;
    }

    /**
     * Da accesso ai metodi dell' interfaccia MedicoSpecDao
     *
     * @return
     */
    @Override
    public JDBCMedicoSpecDao MedicoSpecialista() {
        return medicoSpec;
    }

    /**
     * Da accesso ai metodi dell' interfaccia SspDao
     *
     * @return
     */
    @Override
    public JDBCSspDao Ssp() {
        return ssp;
    }

    public String getUsername(Integer id) throws DaoException {
        String user = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT username FROM utenti WHERE id = ?")) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    user = rs.getString("username");
                }
            } catch (SQLException ex) {
                throw new DaoException("db_error", ex);
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }

        return user;
    }

    @Override
    public Utente getByPrimaryKey(Integer id, String s) throws DaoException {
        Utente ret = null;

        if (id != null) {
            try (PreparedStatement stm = CON.prepareStatement("SELECT u.*, p.nome as nome_provincia,path, c.nome as nome_comune FROM utenti u inner join province p on p.id = u.provincia left join comuni c on c.id = u.comune left join foto f on u.id = f.id_utente WHERE u.id = ?")) {
                stm.setInt(1, id);

                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getString("ruolo").equals("paziente") ||
                                (rs.getString("ruolo").equals("medico") && s.equals("paziente")) ||
                                (rs.getString("ruolo").equals("medico_spec") && s.equals("paziente"))) {
                            ret = new Paziente(rs.getInt("id"), rs.getString("username"), rs.getString("nome"),
                                    rs.getString("cognome"), rs.getDate("data_nascita"), rs.getString("cf"),
                                    rs.getInt("id_medico"), rs.getInt("provincia"), rs.getInt("comune"),
                                    rs.getBoolean("paziente_attivo"), rs.getString("nome_provincia"), rs.getString("nome_comune"), rs.getString("path"), rs.getString("sesso").charAt(0));
                        } else if (rs.getString("ruolo").equals("medico")) {
                            ret = new Medico(rs.getInt("id"), rs.getString("username"), rs.getString("nome"),
                                    rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"),
                                    rs.getBoolean("medico_attivo"), rs.getInt("provincia"), rs.getInt("comune"),
                                    rs.getString("laurea"), rs.getDate("inizio_carriera"), rs.getString("nome_provincia"), rs.getString("nome_comune"), rs.getString("path"), rs.getString("sesso").charAt(0));
                        } else if (rs.getString("ruolo").equals("medico_spec")) {
                            ret = new MedicoSpecialista(rs.getInt("id"), rs.getString("username"), rs.getString("nome"),
                                    rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"),
                                    rs.getBoolean("medico_attivo"), rs.getInt("provincia"), rs.getInt("comune"),
                                    rs.getString("laurea"), rs.getDate("inizio_carriera"), rs.getString("nome_provincia"), rs.getString("nome_comune"), rs.getString("path"), rs.getString("specialita"), rs.getString("sesso").charAt(0));
                        }
                        /*
                        DA COMPLETARE
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
        }
        return ret;
    }


    @Override
    public Integer existsUsername(String username) throws DaoException {
        Integer id = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT id FROM utenti WHERE username = ?")) {
            stm.setString(1, username);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("id");
                }
            } catch (SQLException ex) {
                throw new DaoException("db_error", ex);
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }

        return id;
    }

    @Override
    public String getPasswordToken(String username) throws DaoException {
        String found = null;

        Integer id_utente = existsUsername(username);
        if (id_utente != null) {
            try (PreparedStatement stm = CON.prepareStatement("SELECT hash FROM cambio_password WHERE id_utente = ?")) {
                stm.setInt(1, id_utente);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        found = rs.getString("hash");
                    }
                } catch (SQLException ex) {
                    throw new DaoException("db_error", ex);
                }
            } catch (SQLException ex) {
                throw new DaoException("db_error", ex);
            }
        }

        return found;
    }

    @Override
    public Boolean insertPasswordToken(String username) throws DaoException, NoSuchAlgorithmException {
        Boolean success = false;

        Integer id_utente = existsUsername(username);
        if (id_utente != null) {
            try (PreparedStatement stm = CON.prepareStatement("INSERT INTO cambio_password VALUES (?, ?, ?)")) {
                stm.setInt(1, id_utente);
                stm.setString(2, Common.randomAlphaNumeric());
                stm.setTimestamp(3, new Timestamp(new Date().getTime()));
                Integer rs = stm.executeUpdate();
                if (rs > 0) {
                    success = true;
                }
            } catch (SQLException ex) {
                throw new DaoException("db_error", ex);
            }
        }
        return success;
    }

    @Override
    public Boolean updatePasswordAndRemoveToken(String token, String newPassowrd) throws DaoException {
        Boolean success = false;

        Integer id_utente = null;
        try (PreparedStatement stm = CON.prepareStatement("SELECT id_utente FROM cambio_password WHERE hash = ?")) {
            stm.setString(1, token);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    id_utente = rs.getInt("id_utente");
                }
            } catch (SQLException ex) {
                throw new DaoException("db_error", ex);
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }

        if (id_utente != null) {
            // UPDATE PASSWORD
            try (PreparedStatement stm = CON.prepareStatement("UPDATE utenti SET password = ? WHERE id = ?")) {
                stm.setString(1, Common.getPasswordHash(newPassowrd));
                stm.setInt(2, id_utente);
                Integer rs = stm.executeUpdate();
                if (rs > 0) {
                    success = true;
                }
            } catch (Exception ex) {// prendo tutti gli errori per semplicità
                throw new DaoException("db_error", ex);
            }
            // DELETE TOKEN (hash)
            if (success) {// se ha fatto l'update della password
                try (PreparedStatement stm = CON.prepareStatement("DELETE FROM cambio_password WHERE id_utente = ?")) {
                    stm.setInt(1, id_utente);
                    Integer rs = stm.executeUpdate();
                    //if (rs > 0) {
                    //    success = true;
                    //}
                } catch (SQLException ex) {
                    throw new DaoException("db_error", ex);
                }
            }
        }

        return success;
    }


    /**
     * Il valore di ritorno -2 non è qui usato
     * Il valore di ritorno -4 indica 'id non trovato'
     *
     * @param id
     * @return
     * @throws DaoException
     */
    @Override
    public Utente getByPrimaryKey(Integer id) throws DaoException {
        Utente ret = null;
        int res = -3;

        try (PreparedStatement stm = CON.prepareStatement("SELECT u.*, p.nome as nome_provincia, c.nome as nome_comune FROM utenti u inner join province p on p.id = u.provincia left join comuni c on c.id = u.comune WHERE id = ?")) {
            stm.setInt(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    switch (rs.getString("ruolo")) {
                        case "paziente": ret = getPaziente(rs, 0); break;
                        case "medico": ret = getPersona(rs, 1, "medico"); break;
                        case "medico_spec": ret = getPersona(rs, 1, "medico_spec"); break;  //Ottengo una Persona che indica che la scelta fra Paziente e Medico non è ancora stata fatta
                        case "ssp": ret = getSSP(rs, 2); break;
                    }
                } else {
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
     *
     * @param username
     * @param password
     * @return Un Utente con res = -3 errore metodo, -2 password errata, -1 username non trovato, 0 successo (utente paziente), 1 successo
     * (scelta tra medico e paziente), 2 successo (SSR)
     * @throws it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException
     */
    @Override
    public Utente login(String username, String password) throws DaoException {
        Utente ret = null;
        int res = -3;

        try (PreparedStatement stm = CON.prepareStatement("SELECT u.*, p.nome as nome_provincia, c.nome as nome_comune FROM utenti u inner join province p on p.id = u.provincia left join comuni c on c.id = u.comune WHERE username = ?")) {
            stm.setString(1, username);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    if (Common.validatePassword(password, rs.getString("password"))) {
                        if (getPasswordToken(username) == null) {
                            switch (rs.getString("ruolo")) {
                                case "paziente": ret = getPaziente(rs, 0); break;
                                case "medico": ret = getPersona(rs, 1, "medico"); break;
                                case "medico_spec": ret = getPersona(rs, 1, "medico_spec"); break;  //Ottengo una Persona che indica che la scelta fra Paziente e Medico non è ancora stata fatta
                                case "ssp": ret = getSSP(rs, 2); break;
                            }
                        } else {
                            ret = new Utente(-4); //username trovato, token di reset password esistente
                        }
                    } else {
                        ret = new Utente(-2); //username trovato, password non uguale
                    }
                } else {
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

    private Ssp getSSP(ResultSet rs, int res) throws SQLException {
        return new Ssp(rs.getInt("id"), rs.getString("username"), rs.getInt("provincia"), rs.getString("nome_provincia"), res,
                rs.getString("nome"));
    }

    /**
     * @param rs
     * @param res
     * @return Oggetto Persona con i dati conuni a tutte le persone. In aggiunta il campo ruolo per distinguere tra medico, medico_spec
     * @throws SQLException
     */
    private Persona getPersona(ResultSet rs, int res, String ruolo) throws SQLException {
        return new Persona(rs.getInt("id"), rs.getString("username"), rs.getString("nome"),
                rs.getString("cognome"), rs.getString("cf"), rs.getDate("data_nascita"),
                rs.getInt("provincia"), rs.getInt("comune"), res, ruolo, rs.getString("nome_provincia"), rs.getString("nome_comune"));
    }

    private Paziente getPaziente(ResultSet rs, int res) throws SQLException {
        return new Paziente(rs.getInt("id"), rs.getString("username"), rs.getString("nome"),
                rs.getString("cognome"), rs.getDate("data_nascita"), rs.getString("cf"),
                rs.getInt("id_medico"), rs.getInt("provincia"), rs.getInt("comune"),
                rs.getBoolean("paziente_attivo"), res, rs.getString("nome_provincia"), rs.getString("nome_comune"));
    }

    /**
     * Permette di modificare dati di un Paziente o di un Medico. Altre classi sollevano DaoException
     *
     * @param user Nuovi dati da inserire
     * @return T on success
     * @throws it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException
     */
    @Override
    public boolean update(Utente user) throws DaoException {
        if (user == null) return false;

        try (PreparedStatement ps = getUpdateStatement(user)) {
            int count = ps.executeUpdate();
            if (count == 0) return false;
            else if (count > 1) throw new DaoException("Update affected an invalid number of records: " + count);
            else if (count == 1) return true;

        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return false;
    }

    private PreparedStatement getUpdateStatement(Utente user) throws SQLException, DaoException {
        PreparedStatement ret = null;

        if (user instanceof Paziente) {
            Paziente p = (Paziente) user;
            ret = CON.prepareStatement("UPDATE utenti SET"
                    + " provincia = " + ((p.getProvincia() != null) ? p.getProvincia() : "NULL")// evita che il ret.setString metta gli apici al "NULL"
                    + ", comune = " + ((p.getComune() != null) ? p.getComune() : "NULL")// evita che il ret.setString metta gli apici al "NULL"
                    + ", id_medico = " + ((p.getId_medico() != null) ? p.getId_medico() : "NULL")// evita che il ret.setString metta gli apici al "NULL"
                    + " WHERE id = ? "); //AND ruolo = 'paziente'     rimosso per permettere all'utente medico/paziente di aggiornare
            ret.setInt(1, user.getId());
        } else if (user instanceof Medico) {
            throw new DaoException("Ehi, non so cosa deve modificare. Se hai idee dimmelo :)");
        } else throw new DaoException("update_error");

        return ret;
    }


    /**
     * Ottiene l' elenco delle visite del paziente ordinate in ordine cronologico inverso
     *
     * @param id_paziente
     * @return Elenco delle visite del paziente ordinate in ordine cronologico inverso
     */
    @Override
    public List<Visita> getVisite(Integer id_paziente) throws DaoException {
        if (id_paziente == null || id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        LinkedList<Visita> ret = new LinkedList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT v.*,p.* FROM visita v inner join prescrizione p on p.id = v.id_prescrizione WHERE p.id_paziente = ? ORDER BY time DESC")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Visita r = new Visita(rs.getString("anamnesi"), rs.getInt("id"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getDate("time"));
                ret.add(r);
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    /**
     * Ottiene l' elenco delle visite specialistiche del paziente ordinate in ordine cronologico inverso
     *
     * @param id_paziente
     * @return Elenco delle visite specialistiche del paziente ordinate in ordine cronologico inverso
     * @throws it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException
     */
    @Override
    public List<VisitaSpecialistica> getVisiteSpecialistiche(Integer id_paziente) throws DaoException {
        if (id_paziente == null || id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        List<VisitaSpecialistica> ret = new LinkedList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT v.*,p.*,v2.nome as nome_visita,'not_yet' as nome_medico_spec FROM visita_specialistica v inner join prescrizione p on p.id = v.id_prescrizione inner join visite_specialistiche v2 on v2.id = v.id_visita_spec WHERE p.id_paziente = ? ORDER BY time_visita DESC")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                VisitaSpecialistica r = new VisitaSpecialistica(rs.getInt("id_medico_specialista"), rs.getInt("id_ticket"), rs.getInt("id"), rs.getInt("id_visita_spec"), rs.getString("anamnesi"), rs.getDate("time_visita"), rs.getString("nome_visita"), rs.getString("nome_medico_spec"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getDate("time"), rs.getString("cura"));
                ret.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n\n");
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    /**
     * Ottiene l' elenco delle ricette ordinate in ordine cronologico inverso
     *
     * @param id_paziente
     * @return Elenco delle ricette ordinate in ordine cronologico inverso
     */
    @Override
    public List<Ricetta> getRicette(Integer id_paziente) throws DaoException {
        if (id_paziente == null || id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        List<Ricetta> ret = new LinkedList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT r.*,f.nome,p.* FROM farmaco r inner join farmaci f on f.id = r.id_farmaco inner join prescrizione p on p.id = r.id_prescrizione WHERE id_paziente = ? ORDER BY time DESC")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Ricetta r = new Ricetta(rs.getInt("id_prescrizione"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getInt("id_farmaco"), rs.getString("nome"), rs.getFloat("costo"), rs.getShort("quantita"), rs.getDate("time_vendita"), rs.getDate("time"));
                ret.add(r);
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }


    /**
     * Ottiene l' elenco degli esami del paziente ordinati in ordine cronologico inverso
     *
     * @param id_paziente
     * @return Elenco degli esami del paziente ordinati in ordine cronologico inverso
     */
    @Override
    public List<Esame> getEsami(Integer id_paziente) throws DaoException {
        if (id_paziente == null || id_paziente <= 0) throw new IdNotFoundException("id_paziente");
        LinkedList<Esame> ret = new LinkedList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT r.*,f.nome,p.* FROM esame r inner join esami_prescrivibili f on f.id = r.id_esame inner join prescrizione p on p.id = r.id_prescrizione WHERE id_paziente = ? ORDER BY time_esame DESC")) {
            stm.setInt(1, id_paziente);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Esame e = new Esame(rs.getInt("id_esame"), rs.getInt("id_ticket"), rs.getInt("id_ssp"), rs.getString("risultato"), rs.getDate("time_esame"), rs.getInt("id_prescrizione"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getDate("time"), rs.getString("nome"));
                ret.add(e);
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public List<Esame> getSspEsami(Integer id_ssp) throws DaoException {
        if (id_ssp == null || id_ssp <= 0) throw new IdNotFoundException("id_ssp");
        LinkedList<Esame> ret = new LinkedList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT r.*,f.nome,p.*, u.nome as paz_nome, u.cognome as paz_cognome FROM esame r inner join esami_prescrivibili f on f.id = r.id_esame inner join prescrizione p on p.id = r.id_prescrizione inner join utenti u on p.id_medico = u.id WHERE id_ssp = ? ORDER BY time_esame DESC")) {
            stm.setInt(1, id_ssp);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Esame e = new Esame(rs.getInt("id_esame"), rs.getInt("id_ticket"), rs.getInt("id_ssp"), rs.getString("risultato"), rs.getDate("time_esame"), rs.getInt("id_prescrizione"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getDate("time"), rs.getString("nome"), rs.getString("paz_nome") + " " + rs.getString("paz_cognome"));
                ret.add(e);
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public Farmaci getFarmaci() throws DaoException {
        Farmaci ret = new Farmaci();

        try (PreparedStatement stm = CON.prepareStatement("SELECT id,nome FROM farmaci ORDER BY nome")) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.addFarmaco(rs.getInt("id"), rs.getString("nome"));
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
                ret.addFarmaco(rs.getInt("id"), rs.getString("nome"));
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
                ret.addVisitaSpecialistica(rs.getInt("id"), rs.getString("nome"));
                //ret.add(r);
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public Esami getEsami(String hint) throws DaoException {
        Esami ret = new Esami();

        try (PreparedStatement stm = CON.prepareStatement("SELECT id,nome FROM esami_prescrivibili WHERE nome LIKE ? ORDER BY nome")) {
            stm.setString(1, "%" + hint + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret.addEsame(rs.getInt("id"), rs.getString("nome"));
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public void addLogTime(String url, long time) {
        if (time <= 0) return;

        try {
            Integer new_id = null;
            PreparedStatement ps = CON.prepareStatement("insert into log_time (url,time_took) VALUES (?,?)");
            ps.setString(1, url);
            ps.setLong(2, time);

            int count = ps.executeUpdate();
            if (count == 0)
                System.out.println("Log time inserimento fallito");
        } catch (SQLException ex) {
            System.out.println("Log time execption : \n" + ex.getMessage());
        }
    }

    @Override
    public Visita getVisita(int id_paziente, int id_visita) throws DaoException {
        if (id_visita <= 0 || id_paziente <= 0) throw new IdNotFoundException("ids_error");
        Visita ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT v.*,p.* FROM visita v inner join prescrizione p on p.id = v.id_prescrizione WHERE p.id_paziente = ? AND p.id = ? ORDER BY time DESC")) {
            stm.setInt(1, id_paziente);
            stm.setInt(2, id_visita);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                ret = new Visita(rs.getString("anamnesi"), rs.getInt("id"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getDate("time"));
            }
        } catch (SQLException ex) {
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public VisitaSpecialistica getVisitaSpecialistica(int id_paziente, int id_visita) throws DaoException {
        if (id_visita <= 0 || id_paziente <= 0) throw new IdNotFoundException("ids_error");
        VisitaSpecialistica ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT v.*,p.*,v2.nome as nome_visita,'not_yet' as nome_medico_spec FROM visita_specialistica v inner join prescrizione p on p.id = v.id_prescrizione inner join visite_specialistiche v2 on v2.id = v.id_visita_spec WHERE p.id_paziente = ? AND id_prescrizione = ? ORDER BY time_visita DESC")) {
            stm.setInt(1, id_paziente);
            stm.setInt(2, id_visita);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ret = new VisitaSpecialistica(rs.getInt("id_medico_specialista"), rs.getInt("id_ticket"), rs.getInt("id"), rs.getInt("id_visita_spec"), rs.getString("anamnesi"), rs.getDate("time_visita"), rs.getString("nome_visita"), rs.getString("nome_medico_spec"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getDate("time"), rs.getString("cura"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n\n");
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public Esame getEsame(int id_paziente, int id_esame) throws DaoException {
        if (id_esame <= 0 || id_paziente <= 0) throw new IdNotFoundException("ids_error");
        Esame ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT v.*,p.*,e.nome as nome_esame FROM esame v inner join prescrizione p on p.id = v.id_prescrizione inner join esami_prescrivibili e on e.id = v.id_esame WHERE p.id_paziente = ? AND id_prescrizione = ? ORDER BY time_esame DESC")) {
            stm.setInt(1, id_paziente);
            stm.setInt(2, id_esame);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                ret = new Esame(rs.getInt("id_esame"), rs.getInt("id_ticket"), rs.getInt("id_ssp"), rs.getString("risultato"), rs.getDate("time_esame"), rs.getInt("id_prescrizione"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getDate("time"), rs.getString("nome_esame"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n\n");
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public Ricetta getRicetta(int id_paziente, int id_ricetta) throws DaoException {
        if (id_ricetta <= 0 || id_paziente <= 0) throw new IdNotFoundException("ids_error");
        Ricetta ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT r.*,f.nome,p.* FROM farmaco r inner join farmaci f on f.id = r.id_farmaco inner join prescrizione p on p.id = r.id_prescrizione WHERE id_paziente = ? AND id_prescrizione = ? ORDER BY time DESC")) {
            stm.setInt(1, id_paziente);
            stm.setInt(2, id_ricetta);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                ret = new Ricetta(rs.getInt("id_prescrizione"), rs.getInt("id_paziente"), rs.getInt("id_medico"), rs.getInt("id_farmaco"), rs.getString("nome"), rs.getFloat("costo"), rs.getShort("quantita"), rs.getDate("time_vendita"), rs.getDate("time"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n\n");
            throw new DaoException("db_error", ex);
        }
        return ret;
    }

    @Override
    public Double getImportoTicket(int id_ticket) throws DaoException {
        if (id_ticket <= 0) return null; //non ancora creato
        Double ret = null;

        try (PreparedStatement stm = CON.prepareStatement("SELECT costo FROM ticket WHERE id = ?")) {
            stm.setInt(1, id_ticket);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                ret = rs.getDouble("costo");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n\n");
            throw new DaoException("db_error", ex);
        }
        return ret;
    }
}
