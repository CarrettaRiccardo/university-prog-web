package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 *
 * @author Steve
 */
public class Ricetta {
    private int id_prescrizione, id_farmaco;
    private float costo;
    private short quantita;
    private Date time_vendita;  //Se NULL la ricetta non è ancora stata "ritirata" dal paziente (cioè comprata)

    public Ricetta(int id_prescrizione, int id_farmaco, float costo, short quantita, Date time_vendita) {
        this.id_prescrizione = id_prescrizione;
        this.id_farmaco = id_farmaco;
        this.costo = costo;
        this.quantita = quantita;
        this.time_vendita = time_vendita;
    }

    public int getId_prescrizione() {
        return id_prescrizione;
    }

    public int getId_farmaco() {
        return id_farmaco;
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
    
    
}
