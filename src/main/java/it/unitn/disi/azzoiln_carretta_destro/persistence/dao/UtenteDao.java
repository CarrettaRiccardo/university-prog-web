/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao.Dao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Esame;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Farmaci;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Esami;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.VisiteSpecialistiche;
import java.util.List;

/**
 * Interfaccia della classe UtenteDaoJDBC contenente i metodi che l' implementazione dovrà implementare
 * @author Steve
 */
public interface UtenteDao extends Dao<Utente, Integer>{

    /**
     * Da accesso ai metodi dell' interfaccia MedicoDao
     * @return 
     */
    public MedicoDao Medico();
    /**
     * Da accesso ai metodi dell' interfaccia PazienteDao
     * @return 
     */
    public PazienteDao Paziente();
    /**
     * Da accesso ai metodi dell' interfaccia MedicoSpecDao
     * @return 
     */
    public MedicoSpecDao MedicoSpecialista();
    /**
     * Da accesso ai metodi dell' interfaccia SspDao
     * @return 
     */
    public SspDao Ssp();
    
    /**
     * Da usare se si sa già la modalità con cui si vuole leggere l'utente dal DB
     * @param primaryKey
     * @param modalita "Forza" il tipo di ritorno. Ovvero se un utente è un medico e modalità = 'paziente', allora ottengo come risultato un oggetto di tipo Paziente
     * @return Oggetto che rappresenta l'ogetto che vogliamo ottenere
     * @throws DaoException 
     */
    public Utente getByPrimaryKey(Integer primaryKey, String modalita) throws DaoException;
    
    /**
     * [Per lettura dati Utente] Il comportamento è speculare a login, ma legge i dati partendo dalla PK. Per leggere i dati dopo
     * aver preso id_utente dal REMEMBER ME. Per maggiori info vedi login(..) in JDBCUtenteDao
     * @param primaryKey
     * @return Oggetto che rappresenta l'ogetto che vogliamo ottenere
     * @throws DaoException 
     */
    public Utente getByPrimaryKey(Integer primaryKey) throws DaoException;
    
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
    public Utente login(String username, String password) throws DaoException;
    
    /**
     * 
     * @param user Nuovi dati da inserire
     * @return T on success
     */
    public boolean update(Utente user) throws DaoException;
    
    
    /**
     * Ottiene l' elenco delle visite del paziente ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle visite del paziente ordinate in ordine cronologico inverso
     */
    public List<Visita> getVisite(Integer id_paziente) throws DaoException;
    
    /**
     * Ottiene l' elenco delle visite specialistiche del paziente ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle visite specialistiche del paziente ordinate in ordine cronologico inverso
     * @throws it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException
     */
    public List<VisitaSpecialistica> getVisiteSpecialistiche(Integer id_paziente) throws DaoException;
    
    /**
     * Ottiene l' elenco delle ricette ordinate in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco delle ricette ordinate in ordine cronologico inverso
     */
    public List<Ricetta> getRicette(Integer id_paziente) throws DaoException;
    
    
    /**
     * Ottiene l' elenco degli esami del paziente ordinati in ordine cronologico inverso
     * @param id_paziente
     * @return Elenco degli esami del paziente ordinati in ordine cronologico inverso
     */
    public List<Esame> getEsami(Integer id_paziente) throws DaoException;

    
    /**
     * 
     * @return Elenco di tutti i farmaci disponibili
     */
    public Farmaci getFarmaci() throws DaoException;

    /**
     * Usato da WB farmaci
     * @param hint Stringa cercato dall' utente
     * @return Lista dei farmaci con nome simile ad hint
     * @throws DaoException 
     */
    public Farmaci getFarmaci(String hint) throws DaoException;

    
    /**
     * Usato da WB visite specialistiche
     * @param hint Stringa cercato dall' utente
     * @return Lista delle visite disponibili
     * @throws DaoException 
     */
    public VisiteSpecialistiche getAllVisiteSpec(String hint) throws DaoException;
    
    /**
     * Usato da WB esami
     * @param hint Stringa cercato dall' utente
     * @return Lista dei farmaci con nome simile ad hint
     * @throws DaoException 
     */
    public Esami getEsami(String hint) throws DaoException;

    /**
     * Registra il tempo impiegato per elaborare la risposta. Per statistiche
     * @param requestURI
     * @param l tempo in millisecondi
     */
    public void addLogTime(String requestURI, long time);
}
