package alexjulenerik.demo.model;
//JULEN
import javafx.beans.property.SimpleObjectProperty;

public class Pixel {
    private final int fila;
    private final int columna;
    private final SimpleObjectProperty<EstadoPixel> estado;

    public Pixel(int fila, int columna){
        this.fila=fila;
        this.columna=columna;
        this.estado=new SimpleObjectProperty<>(EstadoPixel.VACIO);
    }

    public EstadoPixel getEstadoPixel() {
        return estado.get();
    }

    public void setEstadoPixel(EstadoPixel pEstadoPixel) {
        this.estado.set(pEstadoPixel);
    }

    public int getFila(){
        return fila;
    }

    public int getColumna(){
        return columna;
    } public boolean estaVacio(){
        if (estado.get() == EstadoPixel.VACIO){
            return true;
        }else {
            return false;
        }
    }

    public SimpleObjectProperty<EstadoPixel> estadoProperty(){
        return estado;
    }

}


