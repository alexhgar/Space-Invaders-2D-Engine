package alexjulenerik.demo.model;

import javafx.application.Platform;
import java.util.Timer;
import java.util.TimerTask;

//ALEX
public class GameModel {
    private static final GameModel instance = new GameModel();
    public static final int FILAS = 60;
    public static final int COLUMNAS = 100;

    private final Pixel[][] matriz;
    private Nave nave;

    private GameModel(){
        matriz = new Pixel[FILAS][COLUMNAS];
        for (int f=0; f< FILAS; f++){
            for(int c=0; c< COLUMNAS; c++){
                matriz[f][c]= new Pixel(f, c);
            }
        }
    }

    public static GameModel getInstance(){
        return instance;
    }

    public void inicializarPartida(){
        for (int f= 0; f< FILAS; f++){
            for( int c=0; c< COLUMNAS; c++){
                matriz[f][c].setEstadoPixel(EstadoPixel.VACIO);
            }
        }

        nave = new Nave(FILAS -5, COLUMNAS/2);
        matriz[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);

        for(int c= 10; c< 90; c +=5){
            matriz[5][c].setEstadoPixel(EstadoPixel.ENEMIGO);
        }
    }

    public Pixel getPixel(int f, int c){
        return matriz[f][c];
    }

    public void moverNaveIzquierda(){
        if (nave != null && nave.getColumna() > 0){
            matriz[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
            nave.setPosicion(nave.getFila(), nave.getColumna() -1);
            matriz[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);

        }
    }

    public void moverNaveDerecha(){
        if( nave != null && nave.getColumna()< COLUMNAS-1){
            matriz[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
            nave.setPosicion(nave.getFila(), nave.getColumna() +1);
            matriz[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);
        }
    }
}
