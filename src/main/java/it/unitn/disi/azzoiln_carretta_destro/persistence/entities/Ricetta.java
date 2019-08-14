package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Steve
 */
public class Ricetta extends Prescrizione{
    private int id_farmaco;
    private String nome_farmaco;
    private float costo;
    private short quantita;
    private Date time_vendita;  //Se NULL la ricetta non è ancora stata "ritirata" dal paziente (cioè comprata)

    /**
     * 
     * @param id_prescrizione
     * @param id_paziente
     * @param id_medico
     * @param id_farmaco
     * @param nome_farmaco
     * @param costo
     * @param quantita
     * @param time_vendita
     * @param time_prescrizione 
     */
    public Ricetta(int id_prescrizione,int id_paziente, int id_medico,int id_farmaco, String nome_farmaco, Float costo, short quantita, Date time_vendita, Date time_prescrizione) {
        super(id_prescrizione, id_paziente, id_medico, time_prescrizione);
        this.id_farmaco = id_farmaco;
        this.nome_farmaco = nome_farmaco;
        this.costo = (costo == null) ? costo : (float)-1.0;
        this.quantita = quantita;
        this.time_vendita = time_vendita;
    }
    
    /**
     * Da usare se si vuole rappresentare una nuova ricetta
     * @param id_paziente
     * @param id_medico
     * @param id_farmaco
     * @param nome_farmaco
     * @param costo
     * @param quantita
     * @param time_vendita
     * @param time_prescrizione 
     */
    public Ricetta(int id_paziente,int id_medico,int id_farmaco, short quantita) {
        super(-1, id_paziente, id_medico, null);
        this.id_farmaco = id_farmaco;
        this.nome_farmaco = null;
        this.costo = -1;
        this.quantita = quantita;
        this.time_vendita = null;
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
        
    public boolean isNew(){
        return (time_vendita == null);
    }

    public int getId_farmaco() {        
        return id_farmaco;
    }
    
    /**
     * Esegue tutta la logica di gestione parametri e controllo valori non validi
     * @param req 
     * @param u Utente di sessione per prelevare id_medico
     * @return  Oggetto Ricetta compilato
     * @throws javax.servlet.ServletException 
     */
    public static Ricetta loadFromHttpRequest(final HttpServletRequest req, final Utente u) throws ServletException{
        int id_paziente = -1, id_farmaco = -1,qta = -1;
        
        if (req.getParameter("id_paziente") == null || req.getParameter("id_farmaco") == null || req.getParameter("qta") == null)
            throw new ServletException("bad_request");
        try {
            id_paziente = Integer.parseInt(req.getParameter("id_paziente"));
            id_farmaco = Integer.parseInt(req.getParameter("id_farmaco"));
            qta = Integer.parseInt(req.getParameter("qta"));
            if (id_paziente <= 0 ) throw new NumberFormatException("id_paziente_not_valid");
            if (id_farmaco <= 0 ) throw new NumberFormatException("id_medico_not_valid");
            if (qta <= 0) throw new NumberFormatException("qta_not_valid");
            if (qta > 200) throw new NumberFormatException("numer_too_big");
        } catch (NumberFormatException e) {
            throw new ServletException(e.getMessage());
        } catch (Exception e) {
            throw new ServletException();
        }
        
        return new Ricetta(id_paziente,u.getId(),id_farmaco, (short) qta);
    }
    
}
