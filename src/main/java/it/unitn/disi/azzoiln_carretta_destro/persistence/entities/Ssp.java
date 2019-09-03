package it.unitn.disi.azzoiln_carretta_destro.persistence.entities;

import java.util.List;

/**
 *
 * @author Steve
 */
public class Ssp extends Utente{

    private String nome;

    public Ssp(int id, String username, Integer provincia,String nome_provincia, int res, String nome) {
        super(id, username, provincia, nome_provincia, null, null, res);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
