package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao.Dao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import java.util.List;

/**
 *
 * @author Steve
 */
public interface MedicoDao extends UtenteDao<Medico>{
    
    /**
     * 
     * @return Elenco di tutti i pazienti del medico
     */
    public List<Paziente> getPazienti(Integer id_medico);
    
    
    
    /**
     * Metodo per registrare la visita di un paziente
     * @param id_medico Id del medico che crea la visita
     * @param id_paziente
     * @param visita I dati della visita da inserire
     * @return T on success
     */
    public boolean addVisita(Integer id_medico,Integer id_paziente,Visita visita);
    
    /**
     * Metodo per registrare la visita di un paziente, assumento i vari ids interni a Visita
     * @param visita I dati della visita da inserire
     * @return T on success
     */
    public boolean addVisita(Visita visita);
    
    /**
     * Aggiunta prescrizione per un farmaco
     * @param id_medico Id del medico che crea la visita
     * @param id_paziente
     * @param id_farmaco 
     * @param qta Quantità del farmaco
     * @return T on success
     */
    public boolean addRicetta(Integer id_medico,Integer id_paziente,Integer id_farmaco, Integer qta);
    
    
    /**
     * @param id_medico Id del medico che crea la visita
     * @param id_paziente
     * @param id_visita_specialistica Id della visita che viene prescitta al paziente. 
     *          Presa da un elenco fisso di possibili visite
     * @return T on success
     */
    public boolean addVisitaSpecialistica(Integer id_medico,Integer id_paziente, Integer id_visita_specialistica);
    
    /**
     * @param id_medico Id del medico che crea la visita
     * @param id_paziente
     * @param id_esame Id dell' esame che viene prescitto al paziente. 
     *          Preso da 'esami_prescivibili' del DB
     * @return T on success
     */
    public boolean addEsame(Integer id_medico,Integer id_paziente, Integer id_esame);
}
