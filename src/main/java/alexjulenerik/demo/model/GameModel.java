package alexjulenerik.demo.model;

import javafx.application.Platform;

import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
//ALEX
public class GameModel {

    //Patron Singleton hecho
    private static final GameModel instance = new GameModel();
    
    //Defino el tamaño de area de nuestro juego
    public static final int FILAS = 60;
    public static final int COLUMNAS = 100;

    //Matriz de objetos pixel que sera nuestra estructura de datos principal
    private final Pixel[][] tablero;
    private Nave nave;
    
    //Listas dinamicas ya que gestionaremos actores que desaparecen y aparecen
    private final List<Enemigo> listaEnemigos = new ArrayList<>();
    private final List<Disparo> listaDisparos = new ArrayList<>();

    //Timers requeridos
    private Timer timerGeneral;
    private int contadorTicks;

    private final SimpleStringProperty estadoJuego = new SimpleStringProperty("ACTIVO");
    
    //Inicializamos cada celda de nuestra matriz con objetos Pixel
    private GameModel(){
        tablero = new Pixel[FILAS][COLUMNAS];
        for (int f=0; f< FILAS; f++){
            for(int c=0; c< COLUMNAS; c++){
                tablero[f][c]= new Pixel(f, c);
            }
        }
    }

    //Metodo para tener acceso al Singleton
    public static GameModel getInstance(){
        return instance;
    }


    public void inicializarPartida(){
        //Reset de tablero/pantalla
        estadoJuego.set("ACTIVO");
        for (int f= 0; f< FILAS; f++){
            for( int c=0; c< COLUMNAS; c++){
                tablero[f][c].setEstadoPixel(EstadoPixel.VACIO);
            }
        }
 
        //Limpiamos colecciones para borrar partidas anteriores
        listaEnemigos.clear();
        listaDisparos.clear();

        //Inicializamos nave en pos 55,50
        nave = new Nave(55, 50);
        tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);

        //Generamos enemigos, entre 4 y 8
        Random random = new Random();
        int numEnemigos = random.nextInt(5) +4;

        for (int i=0; i< numEnemigos; i++){
            int colRandom;
            //Añadido este algoritmo para evitar pisar enbemigos
            do{
                colRandom = random.nextInt(COLUMNAS);
            } while (tablero[5][colRandom].getEstadoPixel() == EstadoPixel.ENEMIGO);

            Enemigo enemigo = new Enemigo(5, colRandom);
            listaEnemigos.add(enemigo);
            tablero[5][colRandom].setEstadoPixel(EstadoPixel.ENEMIGO);

        }

        iniciarTimers();
    }

    public void iniciarTimers(){
        detenerTimers(); // Cancelamos el proceso previo si existe

        timerGeneral = new Timer();
        contadorTicks = 0;

        // Tarea programada a 50ms para gestionar disparos y enemigos en un solo hilo
        timerGeneral.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // El movimiento de disparos ocurre en cada tic (50ms)
                    moverDisparos();

                    contadorTicks++;
                    // Los enemigos se mueven cada 4 tics del timerGeneral (200ms)
                    if (contadorTicks >= 4) {
                        moverEnemigos();
                        contadorTicks = 0;
                    }
                });
            }
        }, 0, 50);
    }


    private void detenerTimers() {
        if (timerGeneral != null){
            timerGeneral.cancel();
            timerGeneral = null;
        }
    }

    //Algoritmo creado para el mviemiento secuencial de los enemigosd
    private void moverEnemigos() {
        if (listaEnemigos.isEmpty()) {
            return;
        } else {
            Random random = new Random();

            //Borramos estado anterior de todos
            for (Enemigo e : listaEnemigos) {
                tablero[e.getFila()][e.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
            }

            //Calculamos nuevas posiciones teniendo limites en cuenta
            boolean finPorDerrota = false;

            Iterator<Enemigo> itEnemigos = listaEnemigos.iterator();

            while (itEnemigos.hasNext()) {
                Enemigo e = itEnemigos.next();
                int direccion = random.nextInt(3);
                int nuevaFila = e.getFila();
                int nuevaCol = e.getColumna();

                if (direccion == 0) {
                    nuevaCol = nuevaCol - 1;
                } else {
                    if (direccion == 1) {
                        nuevaCol = nuevaCol + 1;
                    } else {
                        nuevaFila = nuevaFila + 1;
                    }
                }

                if (nuevaCol < 0) {
                    nuevaCol = 0;
                } else {
                    if (nuevaCol >= COLUMNAS) {
                        nuevaCol = COLUMNAS - 1;
                    }
                }

                boolean enemigoMuerto = false;
                Iterator<Disparo> itDisparo = listaDisparos.iterator();
                while (itDisparo.hasNext() && !enemigoMuerto) {
                    Disparo d = itDisparo.next();
                    if (d.getFila() == nuevaFila && d.getColumna() == nuevaCol) {
                        itEnemigos.remove();
                        itDisparo.remove();
                        //cambiamos el estado del pixel para que desaparezca en caso de impacto
                        tablero[d.getFila()][d.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
                        enemigoMuerto = true;

                    if (listaEnemigos.isEmpty()) {
                        detenerTimers();
                        estadoJuego.set("VICTORIA");
                    }

                }
            }
            if (!enemigoMuerto) {
                if (nuevaFila < FILAS) {
                    boolean colision = false;
                    Iterator<Enemigo> itCompañero = listaEnemigos.iterator();

                    //comprobamos la lista real de enemigos en lugar del dibujo del tablero
                    while (itCompañero.hasNext() && !colision) {
                        Enemigo compañero = itCompañero.next();
                        if (compañero != e && compañero.getFila() == nuevaFila && compañero.getColumna() == nuevaCol) {
                            colision = true; // Activa la bandera y sale del bucle
                        }
                    }

                    if (colision) {
                        //choca con un colega, cancelamos movimiento
                        nuevaFila = e.getFila();
                        nuevaCol = e.getColumna();
                    }
                }



            if (nuevaFila >= FILAS - 1) {
                finPorDerrota = true;
                e.setPosicion(nuevaFila, nuevaCol);
            } else {
                if (nave != null && nuevaFila == nave.getFila() && nuevaCol == nave.getColumna()) {
                    finPorDerrota = true;
                    e.setPosicion(nuevaFila, nuevaCol);
                } else {
                    e.setPosicion(nuevaFila, nuevaCol);
                }
            }
        }
    }

            // Dibuijamos estas posiciones en nuestro tablero
            for (Enemigo e : listaEnemigos){
                if(e.getFila() < FILAS){
                    tablero[e.getFila()][e.getColumna()].setEstadoPixel(EstadoPixel.ENEMIGO);
                }
            }

            if (finPorDerrota){
                detenerTimers();
                estadoJuego.set("DERROTA");
            }
        }
    }

    //Logica de movimiento de los disparos
    private void moverDisparos(){
        Iterator<Disparo> iterator = listaDisparos.iterator();

        while (iterator.hasNext()){
            Disparo d = iterator.next();

            if (d.getFila() >= 0 && d.getFila() < FILAS){
                tablero[d.getFila()][d.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
            }

            int nuevaFila = d.getFila() - 1;
            int col = d.getColumna();

            if (nuevaFila < 0) {
                iterator.remove();
            } else {
                if (tablero[nuevaFila][col].getEstadoPixel() == EstadoPixel.ENEMIGO) {
                    iterator.remove();
                    tablero[nuevaFila][col].setEstadoPixel(EstadoPixel.VACIO);

                    boolean enemigoEncontrado = false;
                    Iterator<Enemigo> itEnemigo = listaEnemigos.iterator();
                    while (itEnemigo.hasNext() && !enemigoEncontrado) {
                        Enemigo e = itEnemigo.next();
                        if (e.getFila() == nuevaFila && e.getColumna() == col) {
                            itEnemigo.remove();
                            enemigoEncontrado = true;
                        }
                    }

                    if (listaEnemigos.isEmpty()) {
                        detenerTimers();
                        estadoJuego.set("VICTORIA");
                    }
                } else {
                    d.setPosicion(nuevaFila, col);
                    tablero[nuevaFila][col].setEstadoPixel(EstadoPixel.DISPARO);
                }
            }
        }
    }

    //Disparo desde nave
    public void disparar(){
        if(nave != null){
            int filaDisparo = nave.getFila()-2;
            if(filaDisparo >= 0) {
                Disparo nuevoDisparo = new Disparo(nave.getFila() - 1, nave.getColumna());
                listaDisparos.add(nuevoDisparo);
                tablero[nuevoDisparo.getFila()][nuevoDisparo.getColumna()].setEstadoPixel(EstadoPixel.DISPARO);
            }
        }
    }


    //Metodo necesario para que nuestr modelo vista/controlador obtenga la info en una casilla
    public Pixel getPixel(int f, int c){
        return tablero[f][c];
    }

    //Metodos para controlar el movimiento de la nave y validando los bordes
    public void moverNaveIzquierda(){
        if (nave != null && nave.getColumna() > 0){
            int nuevaFila = nave.getFila();
            int nuevaCol = nave.getColumna()-1;

            if(tablero[nuevaFila][nuevaCol].getEstadoPixel() == EstadoPixel.ENEMIGO){
              detenerTimers();
              estadoJuego.set("DERROTA");
            }else{
                tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
                nave.setPosicion(nave.getFila(), nave.getColumna() - 1);
                tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);
            }
        }
    }

    public void moverNaveDerecha(){
        if( nave != null && nave.getColumna()< COLUMNAS-1){
            int nuevaFila = nave.getFila();
            int nuevaCol = nave.getColumna()+1;

            if(tablero[nuevaFila][nuevaCol].getEstadoPixel() == EstadoPixel.ENEMIGO){
                detenerTimers();
                estadoJuego.set("DERROTA");
            }else{
                tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
                nave.setPosicion(nave.getFila(), nave.getColumna() + 1);
                tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);
            }
        }
    }

    public void moverNaveArriba() {
        if (nave != null && nave.getFila() > 0) {
            int nuevaFila = nave.getFila()-1;
            int nuevaCol = nave.getColumna();

            if(tablero[nuevaFila][nuevaCol].getEstadoPixel() == EstadoPixel.ENEMIGO){
                detenerTimers();
                estadoJuego.set("DERROTA");
            }else {
                tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
                nave.setPosicion(nave.getFila() - 1, nave.getColumna());
                tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);
            }
        }
    }
        public void moverNaveAbajo(){
            if( nave != null && nave.getFila()<FILAS-2){
                int nuevaFila = nave.getFila()+1;
                int nuevaCol = nave.getColumna();

                if(tablero[nuevaFila][nuevaCol].getEstadoPixel() == EstadoPixel.ENEMIGO){
                    detenerTimers();
                    estadoJuego.set("DERROTA");
                }else {
                    tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.VACIO);
                    nave.setPosicion(nave.getFila() + 1, nave.getColumna());
                    tablero[nave.getFila()][nave.getColumna()].setEstadoPixel(EstadoPixel.NAVE);
                }
            }
    }

    public SimpleStringProperty estadoJuegoProperty() {
        return estadoJuego;
    }
}
