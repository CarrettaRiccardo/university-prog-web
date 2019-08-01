package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Esame;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Prenotazione;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ticket;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Steve
 */
public interface PazienteDao extends UtenteDao<Medico>{
    
    
    /**
     * Modifica il dottore del paziente
     * @param id_paziente
     * @param new_id_medico
     * @return T on success
     */
    public boolean setMedico(Integer id_paziente, Integer new_id_medico);
    
   
    
    
    /**
     * 
     * @param id_medico Id del medico da restituire
     * @return 
     */
    public Medico getMedico(Integer id_medico);
    
    
    /**
     * 
     * @param id_paziente
     * @param id_medico Medico da cui si rivolge
     * @param date Data e ora stabiliti per la prenotazione
     * @return T on success
     */
    public boolean newPrenotazione(Prenotazione prenotazione);
    
    /**
     * 
     * @param id_paziente
     * @return Elenco delle prenotazioni di un paziente da oggi in poi
     */
    public List<Prenotazione> getPrenotazioni(Integer id_paziente);
    
    
    
    /**
     * 
     * @param id_paziente
     * @return Lista ticket pagati/da pagare di un utente
     */
    public List<Ticket> getTickets(Integer id_paziente);
    
}
