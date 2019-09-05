package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 * Classe per rappresentare il Ticket da pagare
 *
 * @author Steve
 */
public class Ticket {
    private int id;
    private float costo;
    private char tipo;
    private Date time;
    private int id_paziente;

    public static final float costo_visite_specialistiche = 50.0f;
    public static final float costo_esami = 11.0f;

    public Ticket(int id, float costo, char tipo, Date time, int id_paziente) {
        this.id = id;
        this.costo = costo;
        this.tipo = tipo;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public float getCosto() {
        return costo;
    }

    public char getTipo() {
        return tipo;
    }

    public Date getTime() {
        return time;
    }

    public int getId_paziente() {
        return id_paziente;
    }
}
