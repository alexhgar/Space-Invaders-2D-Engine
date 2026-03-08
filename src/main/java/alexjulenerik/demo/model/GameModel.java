package alexjulenerik.demo.model;

import javafx.application.Platform;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//ALEX
public class GameModel {
    private static final GameModel instance = new GameModel();
    public static final int FILAS = 60;
    public static final int COLUMNAS = 100;

    private final Pixel[][] tablero;
    private Nave nave;
    
    private final List<Enemigo> listaEnemigos = new ArrayList<>();
    private final List<Enemigo> listaDisparos = new ArrayList<>();

    private int direccionEnemigos = 1;

    private Timer timerEnemigos;
    private Timer timerDisparos;

    private GameModel(){
        tablero = new Pixel[FILAS][COLUMNAS];
        for (int f=0; f< FILAS; f++){
            for(int c=0; c< COLUMNAS; c++){
                tablero[f][c]= new Pixel(f, c);
            }
        }
    }

    public static GameModel getInstance(){
        return instance;
    }


    public void inicializarPartida(){
        for (int f= 0; f< FILAS; f++){
            for( int c=0; c< COLUMNAS; c++){
                tablero[f][c].setEstadoPixel(EstadoPixel.VACIO);
            }
        }

        listaEnemigos.clear();
        listaDisparos.clear();
        direccionEnemigos =1;

        nave = new Nave(55, 50);
        tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);

        Random random = new Random();
        int numEnemigos = random.nextInt(5) +4;

        for (int i=0; i< numEnemigos; i++){
            int colRandom;
            do{
                colRandom = random.nextInt(COLUMNAS);
            } while (tablero[5][colRandom].getEstadoPixel() == EstadoPixel.ENEMIGO);

            Enemigo enemigo = new Enemigo(5, colRandom);
            listaEnemigos.add(Enemigo);
            tablero[5][colRandom].setEstadoPixel(EstadoPixel.ENEMIGO);

        }

        iniciarTimers();
    }

    public void iniciarTimers(){
        if(timerEnemigos != null){
            timerEnemigos.cancel();
        }
        if(timerDisparos != null){
            timerDisparos.cancel();
        }

        timerEnemigos = new Timer();
        timerDisparos = new Timer();

        timerEnemigos.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> moverEnemigos());
            };
    }, 0, 200);

    timerDisparos.scheduleAtFixedRate(new TimerTask() {
        @Override
            public void run(){
                Platform.runLater(() -> moverDisparos())
                     
            };
    } ,0, 50);
    }

    private void moverEnemigos(){
        if (listaEnemigos.isEmpty()) return;

        boolean chocaBorde = false;

        for (Enemigo e : listaEnemigos){
            if (e.getColumna() + direccionEnemigos >= COLUMNAS || e.getColumna() + direccionEnemigos < 0){
                chocaBorde = true;
                break;
            }
        }

        for (Enemigo e : listaEnemigos){
            tablero[e.getFila()][e.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
        }

        if (chocaBorde){
            direccionEnemigos *= -1;
            for (Enemigo e: listaEnemigos){
                e.setPosicion(e.getFila() +1; e.getColumna());
            }
        } else{
            for (Enemigo e : listaEnemigos){
                e.setPosicion(e.getFila(), e.getColumna() + direccionEnemigos);
            }
        }

        for (Enemigo e : listaEnemigos){
            if(e.getFila() < FILAS){
                tablero[e.getFila()][e.getColumna()].setEstadoPixel(EstadoPixel.ENEMIGO);
            }
        }
    }

    private void moverDisparos(){
        Iterator<Disparo> iterator = listaDisparos.iterator();

        while (iterator.hasNext()){
            Disparo d = iterator.next();

            if(d.getFila() >= 0 && d.getFila() < FILAS){
                tablero[d.getFila()][d.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
            }

            d.setPosicion(d.getFila() -1, d.getColumna());

            if(d.getFila()<0){
                iterator.remove();
            } else{
                tablero[d.getFila()][d.getColumna()].setEstadoPixel(EstadoPixel.DISPARO);
            }
        }
    }

    public void disparar(){
        if(nave != null){
            Disparo nuevoDisparo = new Disparo(nave.getFila() -1, nave.getColumna());
            listaDisparos.add(nuevoDisparo);
            tablero[nuevoDisparo.getFila()][nuevoDisparo.getColumna()].setEstadoPixel(EstadoPixel.DISPARO);
        }
    }


    public Pixel getPixel(int f, int c){
        return tablero[f][c];
    }

    public void moverNaveIzquierda(){
        if (nave != null && nave.getColumna() > 0){
            tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
            nave.setPosicion(nave.getFila(), nave.getColumna() -1);
            tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);

        }
    }

    public void moverNaveDerecha(){
        if( nave != null && nave.getColumna()< COLUMNAS-1){
            tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
            nave.setPosicion(nave.getFila(), nave.getColumna() +1);
            tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);
        }
    }
}
