package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.Date;

/**
 *
 * @author Steve
 */
public class MedicoSpecialista extends Persona{
    private boolean attivo;

    public MedicoSpecialista(int id, String username, String nome, String cognome, String cf, Date data_nascita, int provincia, int comune) {
        super(id, username, nome, cognome, cf, data_nascita, provincia, comune, "medico_spec");
    }
}
