package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica;
import java.util.ArrayList;
import java.util.List;

/**
 * Definisco i metodi che le implementazioni devono implementare
 * @author Steve
 */
public interface MedicoSpecDao {
    
    
    /**
     * 
     * @param visita Dati della visita inclusi di id_medico e id_paziente
     * @return T on success
     */
    public boolean newVisita(VisitaSpecialistica visita);
    
    
    /**
     * 
     * @param id_paziente
     * @param id_ticket
     * @return T on success
     */
    public boolean pagaTicket(Integer id_paziente, Integer id_ticket) throws DaoException;
    
    
    /**
     * 
     * @param id_paziente
     * @return  Oggetto Paziente o null on failure
     */
    public Paziente getPaziente(Integer id_paziente) throws DaoException;
    
    
    /**
     * TO BE DEFINED
     * @param id_paziente
     * @return 
     */
    public boolean richiamaPaziente(Integer id_paziente) throws DaoException;

    
    /**
     * Ottiene l'elenco di tutti i pazienti tranne se stesso
     * @param id Id medico loggato
     * @return 
     */
    public List<Paziente> getPazienti(Integer id_medico) throws DaoException;
    
    
    
    /**
     * Aggiunge i dettagli di compilazione alla tupla creata dal MEDICO
     * @param v
     * @param id_medico_spec Id medico che fa la visita spec
     * @return T on success
     * @throws DaoException 
     */
    public boolean compileVisitaSpecialistica(VisitaSpecialistica v, Integer id_medico_spec) throws DaoException;

    public ArrayList<ArrayList<Integer>> getStatsVisiteSpecialistiche(int id) throws DaoException;

    
    /**
     * Tutte le visite passate di mia competenza + visite future o per oggi di mia competenza
     * Non elenca le visite senza data fissata (anche se sono di mia competenza)
     * @param id_paziente
     * @param id_medico_spec
     * @return
     * @throws DaoException 
     */
    public List<VisitaSpecialistica> getVisiteSpecialistiche(Integer id_paziente,Integer id_medico_spec) throws DaoException;

    
    /**
     * Verifica che abbia id_visita tra le competenze
     * @param id_visita
     * @param id
     * @return
     * @throws DaoException 
     */
    public boolean inCompetenza(Integer id_visita, int id_medico) throws DaoException;

    
}
