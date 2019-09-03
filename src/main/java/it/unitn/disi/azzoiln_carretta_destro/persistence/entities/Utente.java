package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

/**
 * @author Steve
 */
public class Utente {
    private int res; //risultato login. Vedi @return di getRes()
    private int id;
    private String username, nome_provincia, nome_comune;
    private Integer provincia, comune;

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

    /**
     * Usato per creare elenco di pazienti/medici..
     *
     * @param id
     * @param username
     */
    public Utente(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public Utente(int id, String username, Integer provincia, String nome_provincia, Integer comune, String nome_comune) {
        this.id = id;
        this.username = username;
        this.provincia = provincia;
        this.nome_provincia = nome_provincia;
        this.comune = comune;
        this.nome_comune = nome_comune;
        res = -99; //valore non inizializzato
    }

    /**
     * Per oggetto recuperato durante il login. Va aggiunto il campo res per individuare errori al login
     */
    public Utente(int id, String username, Integer provincia, String nome_provincia, Integer comune, String nome_comune, int res) {
        this.id = id;
        this.username = username;
        this.provincia = provincia;
        this.nome_provincia = nome_provincia;
        this.comune = comune;
        this.nome_comune = nome_comune;
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

    public Integer getProvincia() {
        return provincia;
    }

    public String getProvinciaNome() {
        return nome_provincia;
    }
    
    public Integer getComune() {
        return comune;
    }

    public String getComuneNome() {
        return nome_comune;
    }

    public boolean isPaziente() {
        return getType() == UtenteType.PAZIENTE;
    }

    public boolean isMedico() {
        return getType() == UtenteType.MEDICO;
    }

    public boolean isMedicoSpecialista() {
        return getType() == UtenteType.MEDICO_SPEC;
    }

    public boolean isSsp() {
        return getType() == UtenteType.SSP;
    }

    public UtenteType getType() {
        if (this instanceof Paziente) return UtenteType.PAZIENTE;
        if (this instanceof Medico) return UtenteType.MEDICO;
        if (this instanceof MedicoSpecialista) return UtenteType.MEDICO_SPEC;
        if (this instanceof Ssp) return UtenteType.SSP;
        return UtenteType.UNKNOWN;
    }
}
