package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

/**
 * @author Steve
 */
public class Utente {
    private int res; //risultato login. Vedi @return di getRes()
    private int id;
    private String username, nome_provincia;
    private int provincia;

    public Utente() {
    }

    /**
     * Usato per ritornare semplicemente errore nel login
     *
     * @param res
     */
    public Utente(int res) {
        this.res = res;
    }

    public Utente(int id, String username, int provincia, String nome_provincia) {
        this.id = id;
        this.username = username;
        this.provincia = provincia;
        this.nome_provincia = nome_provincia;
        res = -99; //valore non inizializzato
    }

    /**
     * Per oggetto recuperato durante il login. Va aggiunto il campo res per individuare errori al login
     */
    public Utente(int id, String username, int provincia,String nome_provincia, int res) {
        this.id = id;
        this.username = username;
        this.provincia = provincia;
        this.nome_provincia = nome_provincia;
        this.res = res;
    }

    /**
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

    public UtenteType getType() {
        if (this instanceof Paziente) return UtenteType.PAZIENTE;
        if (this instanceof Medico) return UtenteType.MEDICO;
        if (this instanceof MedicoSpecialista) return UtenteType.MEDICO_SPEC;
        if (this instanceof Ssp) return UtenteType.SSP;
        return UtenteType.UNKNOWN;
    }
}
