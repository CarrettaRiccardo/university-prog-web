package it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Per rappresentare in modo conveniebnte le strutture dati da passare alla pagina JSP per mostrare i grafici delle stats
 * @author Steve
 */
public class Statistiche<T> {
    private List<T>  dati;
    private String titleGraphic;
    private Integer tipo_grafico; //0->Corechart, 1->Barchart
    

    public Statistiche(){}
    
    public Statistiche(List<T> dati, String titleGraphic,Integer tipo) {
        this.dati = dati;
        this.titleGraphic = titleGraphic; 
        this.tipo_grafico = tipo;
    }

    public List<T> getArrayDati() {
        return dati;
    }

    public String getTitleGraphic() {
        return titleGraphic;
    }
    
    public Integer getTipoGrafico(){
        return tipo_grafico;
    }
    
    
    
    public class LightStats{
        public Integer count;
        public Integer mese;
        public Integer anno;
        public Integer giorno;
        
        public Date data;

        public LightStats(Integer count, Integer mese, Integer anno) {
            this.count = count;
            this.mese = mese;
            this.anno = anno;
        }
        
        public LightStats(Integer count, Date d) {
            this.count = count;
            this.data = d;
        }
        
        public Date getData(){
            return data;
        }
        
        public Integer getCount(){
            return count;
        }
        
    }
    
    
}
