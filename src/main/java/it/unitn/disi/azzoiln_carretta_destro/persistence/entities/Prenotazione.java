package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

/**
 *
 * @author Ricky
 */
public class Prenotazione {
    private int id_paziente;
    private int id_medico;
    private String timestamp;

    public Prenotazione() {
    }
    
    public Prenotazione(int id_paz, int id_med, String data_timestamp) {
        this.id_paziente = id_paz;
        this.id_medico = id_med;
        this.timestamp = data_timestamp;
    }

    public int getIdPaziente() {
        return id_paziente;
    }

    public int getIdMedico() {
        return id_medico;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
}
