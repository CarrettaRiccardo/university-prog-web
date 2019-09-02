package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Steve
 */
public class Visita extends Prescrizione {
    private String anamnesi;

    public Visita(String anamnesi, int id, int id_paziente, int id_medico, Date time) {
        super(id, id_paziente, id_medico, time);
        this.anamnesi = anamnesi;
    }
    
    /**
     * Per creazione nuova Visita
     * @param anamnesi
     * @param id_paziente
     * @param id_medico 
     */
    public Visita(String anamnesi, int id_paziente, int id_medico) {
        super( -1, id_paziente, id_medico, new Date(System.currentTimeMillis()) );
        this.anamnesi = anamnesi;
    }

    public String getAnamnesi() {
        return anamnesi != null ? anamnesi : "";
    }
    
    public String getAnamnesiShort() {
        return anamnesi != null ? anamnesi.substring(0, 20) + ".." : "";
    }
    
    
    /**
     * Esegue tutta la logica di gestione parametri e controllo valori non validi
     * @param req 
     * @param u Utente di sessione per prelevare id_medico
     * @return  Oggetto Ricetta compilato
     * @throws javax.servlet.ServletException 
     */
    public static Visita loadFromHttpRequest(final HttpServletRequest request, final Utente u) throws ServletException{
        int id_paziente = -1;
        String anamnesi = "";

        if (request.getParameter("id_paziente") == null || request.getParameter("anamnesi") == null)
            throw new ServletException("bad_request");
        try {
            anamnesi = request.getParameter("anamnesi");
            id_paziente = Integer.parseInt(request.getParameter("id_paziente"));
            if (id_paziente <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ServletException("id_utente_not_valid");
        } catch (Exception e) {
            throw new ServletException();
        }
        
        return new Visita(anamnesi,id_paziente, u.getId());
    }

}
