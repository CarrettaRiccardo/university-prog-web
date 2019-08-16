/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;
import java.util.List;

/**
 * Struttura classi dettata dal formato che si aspetta in Input il componente Select2
 * @author Steve
 */
public class Farmaci {
    @SerializedName("results")
    private List<Farmaco> list;
    
    public Farmaci(){
        list = new LinkedList<>();
    }
    
    public void addFarmaco(int id, String nome){
        list.add(new Farmaco(id, nome));
    }
    
    //@Expose
    public class Farmaco {
        @SerializedName("id")
        private int id;
        @SerializedName("text")
        private String text;

        public Farmaco(int id, String nome) {
            this.id = id;
            this.text = nome;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNome() {
            return text;
        }

        public void setNome(String nome) {
            this.text = nome;
        }
    }

}


