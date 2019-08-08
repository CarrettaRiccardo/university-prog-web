package it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions;

/**
 * Classe per rappresentare un qualsiasi errore relativo ad un id non trovato.
 * @author Steve
 */
public class IdNotFoundException extends DaoException{
   
    
    /**
     * 
     * @param message Stringa contenente il code di errore (id_paziente | id_medico | id_medico_spec | id_visita | id_farmaco ecc..)
     */
    public IdNotFoundException(String message) {
        super(message);
    }
    
    
    
    public IdNotFoundException(Throwable cause) {
        super(cause);
    }
    
    /**
     * 
     * @param message Stringa contenente il code di errore (id_paziente | id_medico | id_medico_spec | id_visita | id_farmaco ecc..)
     * @param cause 
     */
    public IdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
