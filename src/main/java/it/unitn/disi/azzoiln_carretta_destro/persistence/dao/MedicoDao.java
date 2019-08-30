package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao.Dao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Esame;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Visita;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica;
import java.util.ArrayList;
import java.util.List;

/**
 * Definisco i metodi che le implementazioni devono implementare.
 * Per capire perchè non estende UtenteDao vedi commenti a JDBCMedicoDao
 * @author Steve
 */
public interface MedicoDao{
    
    /**
     * 
     * @return Elenco di tutti i pazienti del medico
     */
    public List<Paziente> getPazienti(Integer id_medico) throws DaoException;
    
    
    
    /**
     * Metodo per registrare la visita di un paziente
     * @param id_medico Id del medico che crea la visita
     * @param id_paziente
     * @param visita I dati della visita da inserire
     * @return T on success
     */
    /*public boolean addVisita(Integer id_medico,Integer id_paziente,Visita visita) throws DaoException;*/
    
    /**
     * Metodo per registrare la visita di un paziente, assumento i vari ids interni a Visita
     * @param visita I dati della visita da inserire
     * @return T on success
     */
    public boolean addVisita(Visita visita) throws DaoException;
    
    /**
     * Aggiunta di una ricetta per un singolo farmaco
     * @param r Ricetta da aggiungere
     * @return
     * @throws DaoException 
     */
    public boolean addRicetta(Ricetta r) throws DaoException;
    
    
    /**
     * @return T on success
     * @throws it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException
     */
    public boolean addVisitaSpecialistica(VisitaSpecialistica v) throws DaoException;
    
    /**
     * @param Esame da inserire
     * @return T on success
     * @throws it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException
     */
    public boolean addEsame(Esame e) throws DaoException;

    /**
     * 
     * @param id_paziente
     * @param id_medico
     * @return T se il Paziente è effettivamente collegato ad quel Medico
     * @throws DaoException 
     */
    public boolean isMyPatient(Integer id_paziente, Integer id_medico) throws DaoException;

    
    /**
     * 
     * @param id 
     */
    public ArrayList< ArrayList<Integer> > getStatsRicette(int id_medico) throws DaoException;

    public ArrayList<ArrayList<Integer>> getStatsVisite(int id_medico) throws DaoException;

    public ArrayList<ArrayList<Integer>> getStatsVisiteSpecialistiche(int id_medico) throws DaoException;
}
