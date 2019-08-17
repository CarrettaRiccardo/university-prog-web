package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

/**
 *
 * @author Ricky
 */
public class Prenotazione {
    private int id_paziente;
    //private String nome_paziente;
    private int id_medico;
    //private String nome_medico;
    private String timestamp;

    public Prenotazione() {
    }
    
    public Prenotazione(int id_paz, int id_med, String data_timestamp) {
        this.id_paziente = id_paz;
        this.id_medico = id_med;
        this.timestamp = data_timestamp;
    }
    
    /*public Prenotazione(int id_paz, int id_med, String data_timestamp, String nome_paz, String nome_med) {
        this.id_paziente = id_paz;
        this.id_medico = id_med;
        this.timestamp = data_timestamp;
        this.nome_paziente = nome_paz;
        this.nome_medico = nome_med;
    }*/

    public int getIdPaziente() {
        return id_paziente;
    }
    
    // per poter mostrare di chi è una prenotazione in modo facile e diretto
    /*public String getNomePaziente() {
        return nome_paziente;
    }*/

    public int getIdMedico() {
        return id_medico;
    }
    
    // per poter mostrare su chi è una prenotazione in modo facile e diretto
    /*public String getNomeMedico() {
        return nome_medico;
    }*/
    
    public String getTimestamp() {
        return timestamp;
    }
}
