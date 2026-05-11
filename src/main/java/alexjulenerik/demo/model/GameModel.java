package alexjulenerik.demo.model;

import alexjulenerik.demo.model.estados.*;
import javafx.application.Platform;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.beans.property.SimpleStringProperty;

public class GameModel {

    private static final GameModel instance = new GameModel();

    public static final int FILAS = 60;
    public static final int COLUMNAS = 100;

    private final Pixel[][] tablero;
    private Nave nave;
    private String tipoNaveSeleccionada = "GREEN";

    private final List<Enemigo> listaEnemigos = new ArrayList<>();
    private final List<Disparo> listaDisparos = new ArrayList<>();

    private Timer timerGeneral;
    private int contadorTicks;

    private long ultimoDisparo = 0;
    private static final long COOLDOWN_DISPARO = 400;

    private EstadoJuego estadoActualObj;
    private final SimpleStringProperty estadoJuego = new SimpleStringProperty("ACTIVO");

    private EstrategiaDisparo estrategiaActual;

    private long tiempoInicio;
    private long tiempoFin;

    private int penalizacionAcumulada;
    private final List<Puntuacion> ranking = new ArrayList<>();

    private String nombreJugador = "Invitado";

    private final javafx.beans.property.IntegerProperty puntuacionVisual = new javafx.beans.property.SimpleIntegerProperty(0);

    private GameModel() {
        tablero = new Pixel[FILAS][COLUMNAS];
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                tablero[f][c] = new Pixel(f, c);
            }
        }
        this.estrategiaActual = new DisparoUnico();
        this.estadoActualObj = new EstadoActivo();
    }

    public static GameModel getInstance() {
        return instance;
    }

    public void cambiarEstado(EstadoJuego nuevoEstado) {
        this.estadoActualObj = nuevoEstado;
        this.estadoJuego.set(nuevoEstado.getNombre());
    }

    private void dibujarActor(Actor actor, EstadoPixel estado) {
        if (actor != null) {
            for (int[] delta : actor.getForma()) {
                int f = actor.getFilaCentral() + delta[0];
                int c = actor.getColumnaCentral() + delta[1];
                if (f >= 0) {
                    if (f < FILAS) {
                        if (c >= 0) {
                            if (c < COLUMNAS) {
                                tablero[f][c].setEstadoPixel(estado);
                            }
                        }
                    }
                }
            }
        }
    }

    public void inicializarPartida() {
        cambiarEstado(new EstadoActivo());

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                tablero[f][c].setEstadoPixel(EstadoPixel.VACIO);
            }
        }

        listaEnemigos.clear();
        listaDisparos.clear();
        estrategiaActual = new DisparoUnico();
        
        tiempoInicio = System.currentTimeMillis();
        tiempoFin =0;
        penalizacionAcumulada =0;

        nave = NaveFactory.crearNave(tipoNaveSeleccionada, 55, 50);
        dibujarActor(nave, EstadoPixel.NAVE);

        Random random = new Random();
        int numEnemigos = random.nextInt(5) + 4;

        for (int i = 0; i < numEnemigos; i++) {
            int colRandom;
            boolean posicionValida;
            int intentos = 0;

            do {
                colRandom = random.nextInt(COLUMNAS - 10) + 5;
                int cTemp = colRandom; // Necesario para la lambda

                // Comprobacion de distancia de enemigos con Streams
                boolean cerca = listaEnemigos.stream().anyMatch(e -> {
                    if (Math.abs(e.getColumnaCentral() - cTemp) <= 3) {
                        return true;
                    } else {
                        return false;
                    }
                });

                if (cerca == true) {
                    posicionValida = false;
                } else {
                    posicionValida = true;
                }

                intentos++;
                if (intentos > 100) {
                    posicionValida = true;
                }
            } while (posicionValida == false && intentos <= 100);

            if (intentos <= 100) {
                Enemigo enemigo = EnemigoFactory.crearEnemigo("BASICO", 5, colRandom);
                listaEnemigos.add(enemigo);
                dibujarActor(enemigo, EstadoPixel.ENEMIGO);
            }
        }

        iniciarTimers();
    }

    public void iniciarTimers() {
        detenerTimers();
        timerGeneral = new Timer();
        contadorTicks = 0;

        timerGeneral.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Llamamos al estado para que decida qué hacer
                    estadoActualObj.actualizarLogica(GameModel.getInstance());
                });
            }
        }, 0, 50);
    }

    private void detenerTimers() {
        if (timerGeneral != null) {
            timerGeneral.cancel();
            timerGeneral = null;
        }
    }



    private boolean chocan(Actor a1, Actor a2) {
        return a1.getForma().stream().anyMatch(delta1 -> {
            int f1 = a1.getFilaCentral() + delta1[0];
            int c1 = a1.getColumnaCentral() + delta1[1];
            return a2.getForma().stream().anyMatch(delta2 -> {
                int f2 = a2.getFilaCentral() + delta2[0];
                int c2 = a2.getColumnaCentral() + delta2[1];
                if (f1 == f2) {
                    if (c1 == c2) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            });
        });
    }

    private boolean tocaFondoONave(Enemigo e) {
        return e.getForma().stream().anyMatch(delta -> {
            int fReal = e.getFilaCentral() + delta[0];
            if (fReal >= FILAS - 1) {
                return true;
            } else {
                if (chocan(e, nave)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }


    public void logicaJuegoActivo() {
        // Borrar dibujos antiguos
        listaDisparos.forEach(d -> dibujarActor(d, EstadoPixel.VACIO));
        listaEnemigos.forEach(e -> dibujarActor(e, EstadoPixel.VACIO));

        // Mover disparos y limpiar los que salen del tablero
        listaDisparos.forEach(d -> d.setPosicionCentral(d.getFilaCentral() - 1, d.getColumnaCentral()));
        listaDisparos.removeIf(d -> d.getForma().stream().anyMatch(delta -> {
            if (d.getFilaCentral() + delta[0] < 0) {
                return true;
            } else {
                return false;
            }
        }));

        // Mover enemigos
        contadorTicks++;
        if (contadorTicks >= 4) {
            Random random = new Random();
            listaEnemigos.forEach(e -> {
                int direccion = random.nextInt(3);
                int nuevaCol = e.getColumnaCentral();
                int nuevaFila = e.getFilaCentral();

                if (direccion == 0) {
                    nuevaCol = nuevaCol - 1;
                } else {
                    if (direccion == 1) {
                        nuevaCol = nuevaCol + 1;
                    } else {
                        nuevaFila = nuevaFila + 1;
                    }
                }

                int colCheck = nuevaCol;
                boolean seSale = e.getForma().stream().anyMatch(delta -> {
                    if (colCheck + delta[1] < 0) {
                        return true;
                    } else {
                        if (colCheck + delta[1] >= COLUMNAS) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                if (seSale == true) {
                    nuevaCol = e.getColumnaCentral();
                }

                int fCheck = nuevaFila;
                int cCheck = nuevaCol;
                boolean chocaCompi = listaEnemigos.stream().anyMatch(otro -> {
                    if (e != otro) {
                        return otro.getForma().stream().anyMatch(deltaOtro -> {
                            int fO = otro.getFilaCentral() + deltaOtro[0];
                            int cO = otro.getColumnaCentral() + deltaOtro[1];
                            return e.getForma().stream().anyMatch(deltaE -> {
                                int fE = fCheck + deltaE[0];
                                int cE = cCheck + deltaE[1];
                                if (fE == fO) {
                                    if (cE == cO) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            });
                        });
                    } else {
                        return false;
                    }
                });

                if (chocaCompi == true) {
                    nuevaFila = e.getFilaCentral();
                    nuevaCol = e.getColumnaCentral();
                }

                e.setPosicionCentral(nuevaFila, nuevaCol);
            });
            contadorTicks = 0;
        }

        List<Disparo> disparosBorrados = listaDisparos.stream()
                .filter(d -> listaEnemigos.stream().anyMatch(e -> chocan(d, e)))
                .toList();

        List<Enemigo> enemigosBorrados = listaEnemigos.stream()
                .filter(e -> disparosBorrados.stream().anyMatch(d -> chocan(d, e)))
                .toList();

        listaDisparos.removeAll(disparosBorrados);
        listaEnemigos.removeAll(enemigosBorrados);
        // Actualizamos la propiedad para que la vista se entere
        puntuacionVisual.set(calcularPuntuacion());

        // Evaluar estado de la partida y redibujar
        if (listaEnemigos.isEmpty()) {
            finalizarPartida(new EstadoVictoria());
        } else {
            boolean derrota = listaEnemigos.stream().anyMatch(this::tocaFondoONave);
            if (derrota == true) {
                finalizarPartida(new EstadoDerrota());
            } else {
                listaEnemigos.forEach(e -> dibujarActor(e, EstadoPixel.ENEMIGO));
                listaDisparos.forEach(d -> dibujarActor(d, EstadoPixel.DISPARO));
            }
        }
    }

    public Pixel getPixel(int f, int c) {
        return tablero[f][c];
    }

    public void moverNaveIzquierda() {
        if (nave != null) {
            if (estadoActualObj instanceof EstadoActivo) {
                boolean puede = true;
                for (int[] delta : nave.getForma()) {
                    int cReal = nave.getColumnaCentral() + delta[1] - 1;
                    if (cReal < 0) {
                        puede = false;
                    }
                }
                if (puede == true) {
                    boolean choca = false;
                    for (int[] delta : nave.getForma()) {
                        int fReal = nave.getFilaCentral() + delta[0];
                        int cReal = nave.getColumnaCentral() + delta[1] - 1;
                        if (tablero[fReal][cReal].getEstadoPixel() == EstadoPixel.ENEMIGO) {
                            choca = true;
                        }
                    }

                    if (choca == true) {
                        finalizarPartida(new EstadoDerrota());
                    } else {
                        dibujarActor(nave, EstadoPixel.VACIO);
                        nave.setPosicionCentral(nave.getFilaCentral(), nave.getColumnaCentral() - 1);
                        dibujarActor(nave, EstadoPixel.NAVE);
                    }
                }
            }
        }
    }

    public void moverNaveDerecha() {
        if (nave != null) {
            if (estadoActualObj instanceof EstadoActivo) {
                boolean puede = true;
                for (int[] delta : nave.getForma()) {
                    int cReal = nave.getColumnaCentral() + delta[1] + 1;
                    if (cReal >= COLUMNAS) {
                        puede = false;
                    }
                }
                if (puede == true) {
                    boolean choca = false;
                    for (int[] delta : nave.getForma()) {
                        int fReal = nave.getFilaCentral() + delta[0];
                        int cReal = nave.getColumnaCentral() + delta[1] + 1;
                        if (tablero[fReal][cReal].getEstadoPixel() == EstadoPixel.ENEMIGO) {
                            choca = true;
                        }
                    }

                    if (choca == true) {
                        finalizarPartida(new EstadoDerrota());
                    } else {
                        dibujarActor(nave, EstadoPixel.VACIO);
                        nave.setPosicionCentral(nave.getFilaCentral(), nave.getColumnaCentral() + 1);
                        dibujarActor(nave, EstadoPixel.NAVE);
                    }
                }
            }
        }
    }

    public void moverNaveArriba() {
        if (nave != null) {
            if (estadoActualObj instanceof EstadoActivo) {
                boolean puede = true;
                for (int[] delta : nave.getForma()) {
                    int fReal = nave.getFilaCentral() + delta[0] - 1;
                    if (fReal < 0) {
                        puede = false;
                    }
                }
                if (puede == true) {
                    boolean choca = false;
                    for (int[] delta : nave.getForma()) {
                        int fReal = nave.getFilaCentral() + delta[0] - 1;
                        int cReal = nave.getColumnaCentral() + delta[1];
                        if (tablero[fReal][cReal].getEstadoPixel() == EstadoPixel.ENEMIGO) {
                            choca = true;
                        }
                    }

                    if (choca == true) {
                        finalizarPartida(new EstadoDerrota());
                    } else {
                        dibujarActor(nave, EstadoPixel.VACIO);
                        nave.setPosicionCentral(nave.getFilaCentral() - 1, nave.getColumnaCentral());
                        dibujarActor(nave, EstadoPixel.NAVE);
                    }
                }
            }
        }
    }

    public void moverNaveAbajo() {
        if (nave != null) {
            if (estadoActualObj instanceof EstadoActivo) {
                boolean puede = true;
                for (int[] delta : nave.getForma()) {
                    int fReal = nave.getFilaCentral() + delta[0] + 1;
                    if (fReal >= FILAS) {
                        puede = false;
                    }
                }
                if (puede == true) {
                    boolean choca = false;
                    for (int[] delta : nave.getForma()) {
                        int fReal = nave.getFilaCentral() + delta[0] + 1;
                        int cReal = nave.getColumnaCentral() + delta[1];
                        if (tablero[fReal][cReal].getEstadoPixel() == EstadoPixel.ENEMIGO) {
                            choca = true;
                        }
                    }

                    if (choca == true) {
                        finalizarPartida(new EstadoDerrota());
                    } else {
                        dibujarActor(nave, EstadoPixel.VACIO);
                        nave.setPosicionCentral(nave.getFilaCentral() + 1, nave.getColumnaCentral());
                        dibujarActor(nave, EstadoPixel.NAVE);
                    }
                }
            }
        }
    }

    public void setTipoNaveSeleccionada(String tipoNave) {
        this.tipoNaveSeleccionada = tipoNave;
    }

    public String getTipoNaveSeleccionada() {
        return this.tipoNaveSeleccionada;
    }

    public void setNombreJugador(String nombre) {
        this.nombreJugador = nombre;
    }

    public SimpleStringProperty estadoJuegoProperty() {
        return estadoJuego;
    }

    public void disparar() {
        if (nave != null || !(estadoActualObj instanceof EstadoActivo)) {
            if (estadoActualObj instanceof EstadoActivo) {
                long ahora = System.currentTimeMillis();
                if (ahora - ultimoDisparo < COOLDOWN_DISPARO){
                    return;
                }

                int minDeltaNave = nave.getForma().stream().mapToInt(delta -> delta[0]).min().orElse(0);

                int filaMasAltaNave = nave.getFilaCentral() + minDeltaNave;
                int filaFondoDisparo = filaMasAltaNave - 1;

                if (filaFondoDisparo >= 0) {
                    estrategiaActual.realizarDisparo(filaFondoDisparo, nave.getColumnaCentral(), listaDisparos);

                    if (listaDisparos.isEmpty() == false) {
                        Disparo nuevoDisparo = listaDisparos.get(listaDisparos.size() - 1);
                        dibujarActor(nuevoDisparo, EstadoPixel.DISPARO);
                        ultimoDisparo = ahora;
                    }

                    if(estrategiaActual instanceof DisparoFlecha){
                        penalizacionAcumulada += 15;
                    
                    }else if(estrategiaActual instanceof DisparoRombo){
                        penalizacionAcumulada += 30;
                    }
                }
            }
        }
    }

    public void cambiarArma() {
        if (tipoNaveSeleccionada.equals("GREEN")) {
            if (estrategiaActual instanceof DisparoUnico) {
                estrategiaActual = new DisparoFlecha();
            } else {
                estrategiaActual = new DisparoUnico();
            }
        } else {
            if (tipoNaveSeleccionada.equals("BLUE")) {
                if (estrategiaActual instanceof DisparoUnico) {
                    estrategiaActual = new DisparoRombo();
                } else {
                    estrategiaActual = new DisparoUnico();
                }
            } else {
                if (tipoNaveSeleccionada.equals("RED")) {
                    if (estrategiaActual instanceof DisparoUnico) {
                        estrategiaActual = new DisparoFlecha();
                    } else {
                        if (estrategiaActual instanceof DisparoFlecha) {
                            estrategiaActual = new DisparoRombo();
                        } else {
                            estrategiaActual = new DisparoUnico();
                        }
                    }
                }
            }
        }
    }

    public int calcularPuntuacion(){
        if (estadoActualObj instanceof EstadoDerrota) {
            return 0;
        }

        long finEfectivo = (tiempoFin > 0) ? tiempoFin : System.currentTimeMillis();
        long segundos = (finEfectivo - tiempoInicio)/1_000;
        int puntos = (int)(10_000 - segundos*100 - penalizacionAcumulada);
        return Math.max(0, puntos);
    }

    public void registrarPuntuacion(String nombreJugador){
        int puntos =calcularPuntuacion();
        ranking.add(new Puntuacion(nombreJugador, puntos));
    }

    public List<Puntuacion> getTopRanking(){
        return ranking
        .stream()
        .sorted(Comparator.comparingInt(Puntuacion::getPuntos).reversed())
        .limit(10)
        .toList();
    }

    

    public int getPenalizacionAcumulada(){
        return penalizacionAcumulada;
    }

    public int getPuntuacionParcial(){
        return calcularPuntuacion();
    }

    public javafx.beans.property.IntegerProperty puntuacionVisualProperty() {
        return puntuacionVisual;
    }

    private void finalizarPartida(EstadoJuego estadoFinal) {
        detenerTimers();
        tiempoFin = System.currentTimeMillis();
        cambiarEstado(estadoFinal);
        registrarPuntuacion(nombreJugador);
    }

}