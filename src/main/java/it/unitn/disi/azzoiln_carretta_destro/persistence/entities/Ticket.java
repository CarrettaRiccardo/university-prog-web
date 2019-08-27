package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 * Classe per rappresentare il Ticket da pagare
 * @author Steve
 */
public class Ticket {
    private int id;
    private float costo;
    private char tipo;
    private Date time;
    
    public static final float costo_visite_specialistiche = (float) 50.0;

    public Ticket(int id, float costo, char tipo, Date time) {
        this.id = id;
        this.costo = costo;
        this.tipo = tipo;
        this.time = time;
    }
    
    
}
