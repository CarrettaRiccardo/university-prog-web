package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Esame;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Prenotazione;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ticket;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import java.util.Calendar;
import java.util.List;

/**
 * Definisco i metodi che le implementazioni devono implementare
 * @author Steve
 */
public interface PazienteDao{
    
    
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
     * @return T on success
     */
    public boolean newPrenotazione(Prenotazione prenotazione);
    
    /**
     * 
     * @param id_paziente
     * @return Elenco delle prenotazioni di quel giorno
     */
    public List<Prenotazione> getPrenotazioni(String data) throws DaoException;
    
    
        /**
     * 
     * @param id_paziente
     * @return Elenco delle prenotazioni di un paziente
     */
    public List<Prenotazione> getPrenotazioni(Integer id_paziente) throws DaoException;
    
    
    
    /**
     * 
     * @param id_paziente
     * @return Lista ticket pagati/da pagare di un utente
     */
    public List<Ticket> getTickets(Integer id_paziente);
    
}
