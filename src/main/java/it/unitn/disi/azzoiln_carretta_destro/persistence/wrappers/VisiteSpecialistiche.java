/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers;

import com.google.gson.annotations.SerializedName;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.VisitaSpecialistica;
import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper per VisitaSpecilistica per serializzazione
 * @author Steve
 */
public class VisiteSpecialistiche {
    @SerializedName("results")
    private List<LightVisita> list;
    
    public VisiteSpecialistiche(){
        list = new LinkedList<>();
    }
    
    public void addVisitaSpecialistica(int id, String nome){
        list.add(new LightVisita(id, nome));
    }
    
    /**
     * Solo come contenitore di dati
     */
    public class LightVisita{
        @SerializedName("id")
        public int id;
        @SerializedName("text")
        public String nome;

        public LightVisita(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }
        
        
    }
}
