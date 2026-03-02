package alexjulenerik.demo.model;
//JULEN
public abstract class Actor {
    private int fila;
    private int columna;

    public Actor(int fila, int columna){
        this.fila=fila;
        this.columna=columna;
    }

    public int getFila(){
        return fila;
    }

    public int getColumna(){
        return columna;
    }

    public void setPosicion(int fila, int columna){
        this.fila=fila;
        this.columna=columna;
    }

}
