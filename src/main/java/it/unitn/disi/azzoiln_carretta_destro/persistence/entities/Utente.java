package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

/**
 *
 * @author Steve
 */
public class Utente {
    private int res; //risultato login. Vedi @return di getRes()
    private int id;
    private String username;
    private int provincia;
    
    /**
     * Usato per ritornare semplicemente errore nel login
     * @param res 
     */
    public Utente(int res){
        this.res = res;
    }
    
    public Utente(int id, String username, int provincia){
        this.id = id;
        this.username = username;
        this.provincia = provincia;
        res = -99; //valore non inizializzato
    }
    
    public Utente(int id, String username,int provincia, int res){
        this.id = id;
        this.username = username;
        this.provincia = provincia;
        this.res = res;
    }
    
    
    /**
     * 
     * @return -3 errore metodo, -2 password errata, -1 username non trovato, 0 successo (utente paziente), 1 successo 
     * (scelta tra medico e paziente), 2 successo (SSR)
     */
    public int getRes() {
        return res;
    }
    
    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }
    
    public int getProvincia() {
        return provincia;
    }
}
