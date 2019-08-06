package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 *
 * @author Steve
 */
public class Ricetta {
    private int id_prescrizione, id_paziente, id_medico, id_farmaco;
    private String nome_farmaco;
    private float costo;
    private short quantita;
    private Date time_vendita, time_prescrizione;  //Se NULL la ricetta non è ancora stata "ritirata" dal paziente (cioè comprata)

    /**
     * 
     * @param id_prescrizione
     * @param id_paziente
     * @param nome_farmaco
     * @param costo
     * @param quantita
     * @param time_vendita
     * @param time_prescrizione 
     */
    public Ricetta(int id_prescrizione,int id_paziente, int id_medico,int id_farmaco, String nome_farmaco, float costo, short quantita, Date time_vendita, Date time_prescrizione) {
        this.id_prescrizione = id_prescrizione;
        this.id_paziente = id_paziente;
        this.id_medico = id_medico;
        this.id_farmaco = id_farmaco;
        this.nome_farmaco = nome_farmaco;
        this.costo = costo;
        this.quantita = quantita;
        this.time_vendita = time_vendita;
        this.time_prescrizione = time_prescrizione;
    }
    
    /**
     * Da usare se si vuole rappresentare una nuova ricetta
     * @param id_paziente
     * @param nome_farmaco
     * @param costo
     * @param quantita
     * @param time_vendita
     * @param time_prescrizione 
     */
    public Ricetta(int id_paziente,int id_medico,int id_farmaco, String nome_farmaco, Date time_prescrizione) {
        this.id_paziente = id_paziente;
        this.id_medico = id_medico;
        this.id_farmaco = id_farmaco;
        this.nome_farmaco = nome_farmaco;
        this.costo = -1;
        this.quantita = -1;
        this.time_vendita = null;
        this.time_prescrizione = time_prescrizione;
    }

    public int getId_prescrizione() {
        return id_prescrizione;
    }
    
    public int getId_paziente() {
        return id_paziente;
    }
    
    public int getId_medico() {
        return id_paziente;
    }

    public String getNomeFarmaco() {
        return nome_farmaco;
    }

    public float getCosto() {
        return costo;
    }

    public short getQuantita() {
        return quantita;
    }

    public Date getTime_vendita() {
        return time_vendita;
    }
    
    public Date getTime_prescrizione() {
        return time_prescrizione;
    }
    
    public boolean isNew(){
        return (time_vendita == null);
    }

    public int getId_farmaco() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
