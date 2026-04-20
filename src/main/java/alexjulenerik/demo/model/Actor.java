package alexjulenerik.demo.model;

import java.util.ArrayList;
import java.util.List;

//JULEN
public abstract class Actor {
    private int filaCentral;
    private int columnaCentral;

    // Lista de desplazamientos respecto al centro
    protected List<int[]> forma;

    public Actor(int filaCentral, int columnaCentral){
        this.filaCentral = filaCentral;
        this.columnaCentral = columnaCentral;
        this.forma = new ArrayList<>();
    }

    public int getFilaCentral(){
        return filaCentral;
    }

    public int getColumnaCentral(){
        return columnaCentral;
    }

    public void setPosicionCentral(int fila, int columna){
        this.filaCentral = fila;
        this.columnaCentral = columna;
    }

    public List<int[]> getForma() {
        return forma;
    }
}