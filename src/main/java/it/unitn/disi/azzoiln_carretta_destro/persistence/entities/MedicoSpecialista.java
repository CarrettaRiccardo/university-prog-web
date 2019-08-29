package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 * @author Steve
 */
public class MedicoSpecialista extends Persona {
    private String laurea;
    private String specialita;
    private Date inizioCarriera;
    private boolean attivo;

    public MedicoSpecialista(int id, String username, String nome, String cognome, String cf, Date data_nascita, boolean attivo, int provincia, int comune, String laurea, Date inizioCarriera, String nome_prov, String foto, String specialita) {
        super(id, username, nome, cognome, cf, data_nascita, provincia, comune, "medico_spec", nome_prov, foto);
        this.laurea = laurea;
        this.specialita = specialita;
        this.inizioCarriera = inizioCarriera;
        this.attivo = attivo;
    }

    public MedicoSpecialista(Persona p, String laurea, String specialita, Date inizioCarriera, boolean attivo, String foto) {
        super(p.getId(), p.getUsername(), p.getNome(), p.getCognome(), p.getCf(), p.getData_nascita(), p.getProvincia(), p.getId_Comune(), "medico_spec", p.getNome_comune(), foto);
        this.laurea = laurea;
        this.specialita = specialita;
        this.inizioCarriera = inizioCarriera;
        this.attivo = attivo;
    }

    public String getLaurea() {
        return laurea;
    }

    public Date getInizioCarriera() {
        return inizioCarriera;
    }

    public String getSpecialita() {
        return specialita;
    }
}
