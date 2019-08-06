package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica;

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
    public boolean pagaTicket(Integer id_paziente, Integer id_ticket);
    
    
    /**
     * 
     * @param id_paziente
     * @return  Oggetto Paziente o null on failure
     */
    public Paziente getPaziente(Integer id_paziente);
    
    
    /**
     * TO BE DEFINED
     * @param id_paziente
     * @return 
     */
    public boolean richiamaPaziente(Integer id_paziente);
    
}
