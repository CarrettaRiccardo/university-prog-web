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

    public MedicoSpecialista(int id, String username, String nome, String cognome, String cf, Date data_nascita, boolean attivo, Integer provincia, Integer comune, String laurea, Date inizioCarriera, String nome_prov, String nome_com, String foto, String specialita,char sesso) {
        super(id, username, nome, cognome, cf, data_nascita, provincia, comune, "medico_spec", nome_prov, nome_com, foto,sesso);
        this.laurea = laurea;
        this.specialita = specialita;
        this.inizioCarriera = inizioCarriera;
        this.attivo = attivo;
    }

    public MedicoSpecialista(Persona p, String laurea, String specialita, Date inizioCarriera, boolean attivo, String foto,char sesso) {
        super(p.getId(), p.getUsername(), p.getNome(), p.getCognome(), p.getCf(), p.getData_nascita(), p.getProvincia(), p.getComune(), "medico_spec", p.getProvinciaNome(), p.getComuneNome(), foto,sesso);
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
