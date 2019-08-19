package it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers;

import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper per Esame per serializzazione
 * @author Steve
 */
public class Esami {
    @SerializedName("results")
    private List<LightEsame> list;
    
    public Esami(){
        list = new LinkedList<>();
    }
    
    public void addEsame(int id, String nome){
        list.add(new LightEsame(id, nome));
    }
    
    /**
     * Solo come contenitore di dati
     */
    public class LightEsame{
        @SerializedName("id")
        public int id;
        @SerializedName("text")
        public String nome;

        public LightEsame(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }
        
        
    }
}
