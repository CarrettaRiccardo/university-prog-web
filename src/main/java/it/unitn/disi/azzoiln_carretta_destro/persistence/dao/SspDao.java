package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Esame;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ssp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servizio snitario provinciale
 * @author Steve
 */
public interface SspDao{
    
    
    /**
     * TO BE DEFINED
     * @param id_esame
     * @return 
     */
    public boolean erogaEsame(Esame e) throws DaoException;
    
    /**
     * Ottiene l' elenco degli esami dei pazienti del SSP ordinati in ordine cronologico inverso
     *
     * @param id_ssp
     * @return Elenco degli esami dei pazienti del SSP ordinati in ordine cronologico inverso
     */
    public List<Esame> getEsami(Integer id_ssp) throws DaoException;
    
    public List<Medico> getMedici(Integer id_provincia) throws DaoException;

    public List<MedicoSpecialista> getMediciSpecialisti(Integer id_provincia) throws DaoException;
    
    public List<Paziente> getPazienti(Integer id_paziente) throws DaoException;
    
    public String getNomeProvincia(Integer id_provincia) throws DaoException;
    
    public Integer getIdProvincia(String nome_provincia) throws DaoException;
    
    public String getNomeComune(Integer id_comune) throws DaoException;
    
    public Integer getIdComune(String nome_comune) throws DaoException;
    
    public List<String> getListProvince() throws DaoException;
    
    public List<String> getListComuni(Integer id_prov) throws DaoException;

    public ArrayList<ArrayList<Integer>> getStatsEsami(int id) throws DaoException;

    public List<Ricetta> getRicette(Date data, Integer id_provincia) throws DaoException;
}
