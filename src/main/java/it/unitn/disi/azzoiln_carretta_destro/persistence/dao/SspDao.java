package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ssp;

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
    
}
