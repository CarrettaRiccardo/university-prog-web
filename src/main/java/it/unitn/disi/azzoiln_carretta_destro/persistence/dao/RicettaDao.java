package it.unitn.disi.azzoiln_carretta_destro.persistence.dao;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.dao.Dao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import java.util.List;

/**
 *
 * @author Steve
 */
public interface RicettaDao extends Dao<Ricetta, Integer>{
    
    public List<Ricetta> getRicette(Integer id_paziente) throws DaoException;
    
    public boolean addRicetta(Ricetta r) throws DaoException;
}
