package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ssp;
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
    public boolean erogaEsame(Integer id_esame);
    
    public List<Medico> getMedici(Integer id_provincia) throws DaoException;

    public List<MedicoSpecialista> getMediciSpecialisti(Integer id_provincia) throws DaoException;
    
    public List<Paziente> getPazienti(Integer id_paziente) throws DaoException;
    
    public String getNomeProvincia(Integer id_provincia) throws DaoException;
    
    public Integer getIdProvincia(String nome_provincia) throws DaoException;
    
    public List<String> getListProvince() throws DaoException;
}
