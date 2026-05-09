package alexjulenerik.demo.model;

public class Puntuacion {

    private String nombre;
    private int puntos;

    public Puntuacion(String nombre, int puntos){
        if (nombre == null || nombre.isBlank() ){
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public String getNombre(){
        return nombre;
    }

    public int getPuntos(){
        return puntos;
    }

    @Override
    public String toString(){
        return nombre + "->" + puntos + "pts";
    }

}
