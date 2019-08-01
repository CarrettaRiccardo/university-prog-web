package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

/**
 * Servizio snitario provinciale
 * @author Steve
 */
public interface Ssp extends UtenteDao<Ssp>{
    
    
    /**
     * TO BE DEFINED
     * @param id_esame
     * @return 
     */
    public boolean erogaEsame(Integer id_esame);
    
}
