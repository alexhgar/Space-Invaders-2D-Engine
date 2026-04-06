package alexjulenerik.demo.model;

import javafx.application.Platform;

import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

//ALEX (Adaptado para HU7 y HU9 - Bug de congelación arreglado)
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

    private final SimpleStringProperty estadoJuego = new SimpleStringProperty("ACTIVO");

    private EstrategiaDisparo estrategiaActual;

    private GameModel() {
        tablero = new Pixel[FILAS][COLUMNAS];
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                tablero[f][c] = new Pixel(f, c);
            }
        }
        this.estrategiaActual = new DisparoUnico();
    }

    public static GameModel getInstance() {
        return instance;
    }

    // NUEVO MÉTODO AUXILIAR: Dibuja o borra un actor completo leyendo su lista de forma
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
        estadoJuego.set("ACTIVO");
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                tablero[f][c].setEstadoPixel(EstadoPixel.VACIO);
            }
        }

        listaEnemigos.clear();
        listaDisparos.clear();

        // Reseteamos el arma al disparo básico al empezar
        estrategiaActual = new DisparoUnico();

        // Usamos la factoría para crear la nave (por defecto GREEN para probar)

        nave = NaveFactory.crearNave(tipoNaveSeleccionada, 55, 50);
        dibujarActor(nave, EstadoPixel.NAVE);

        Random random = new Random();
        int numEnemigos = random.nextInt(5) + 4;

        for (int i = 0; i < numEnemigos; i++) {
            int colRandom;
            boolean posicionValida = false;
            do {
                colRandom = random.nextInt(COLUMNAS - 4) + 2;
                posicionValida = true;
                if (tablero[5][colRandom].getEstadoPixel() == EstadoPixel.ENEMIGO) {
                    posicionValida = false;
                }
            } while (posicionValida == false);

            // Usamos la factoría para crear el enemigo
            Enemigo enemigo = EnemigoFactory.crearEnemigo("BASICO", 5, colRandom);
            listaEnemigos.add(enemigo);
            dibujarActor(enemigo, EstadoPixel.ENEMIGO);
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
                    moverDisparos();
                    contadorTicks++;
                    if (contadorTicks >= 4) {
                        moverEnemigos();
                        contadorTicks = 0;
                    }
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

    private void moverEnemigos() {
        if (listaEnemigos.isEmpty() == false) {
            Random random = new Random();
            boolean finPorDerrota = false;

            Iterator<Enemigo> itEnemigos = listaEnemigos.iterator();

            while (itEnemigos.hasNext()) {
                Enemigo e = itEnemigos.next();

                // Borramos solo el enemigo actual usando su forma
                dibujarActor(e, EstadoPixel.VACIO);

                int direccion = random.nextInt(3);
                int nuevaFila = e.getFilaCentral();
                int nuevaCol = e.getColumnaCentral();

                if (direccion == 0) {
                    nuevaCol = nuevaCol - 1;
                } else {
                    if (direccion == 1) {
                        nuevaCol = nuevaCol + 1;
                    } else {
                        nuevaFila = nuevaFila + 1;
                    }
                }

                boolean limiteSuperado = false;
                for (int[] delta : e.getForma()) {
                    int colReal = nuevaCol + delta[1];
                    if (colReal < 0) {
                        limiteSuperado = true;
                    } else {
                        if (colReal >= COLUMNAS) {
                            limiteSuperado = true;
                        }
                    }
                }

                if (limiteSuperado == true) {
                    nuevaCol = e.getColumnaCentral();
                }

                boolean enemigoMuerto = false;
                Iterator<Disparo> itDisparo = listaDisparos.iterator();

                // ARREGLO 1: Seguro anti-doble borrado
                while (itDisparo.hasNext() && enemigoMuerto == false) {
                    Disparo d = itDisparo.next();
                    for (int[] delta : e.getForma()) {
                        int fReal = nuevaFila + delta[0];
                        int cReal = nuevaCol + delta[1];

                        for (int[] deltaDisp : d.getForma()) {
                            int fDisp = d.getFilaCentral() + deltaDisp[0];
                            int cDisp = d.getColumnaCentral() + deltaDisp[1];

                            if (fDisp == fReal) {
                                if (cDisp == cReal) {
                                    // Comprobamos que no lo hayamos borrado ya en este mismo chequeo
                                    if (enemigoMuerto == false) {
                                        itEnemigos.remove();
                                        itDisparo.remove();
                                        dibujarActor(d, EstadoPixel.VACIO);
                                        enemigoMuerto = true;
                                        if (listaEnemigos.isEmpty()) {
                                            detenerTimers();
                                            estadoJuego.set("VICTORIA");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (enemigoMuerto == false) {
                    boolean colision = false;
                    for (int[] delta : e.getForma()) {
                        int fReal = nuevaFila + delta[0];
                        int cReal = nuevaCol + delta[1];
                        if (fReal < FILAS) {
                            if (tablero[fReal][cReal].getEstadoPixel() == EstadoPixel.ENEMIGO) {
                                colision = true;
                            }
                        }
                    }

                    if (colision == true) {
                        nuevaFila = e.getFilaCentral();
                        nuevaCol = e.getColumnaCentral();
                    }

                    boolean tocaFondo = false;
                    boolean tocaNave = false;
                    for (int[] delta : e.getForma()) {
                        int fReal = nuevaFila + delta[0];
                        int cReal = nuevaCol + delta[1];
                        if (fReal >= FILAS - 1) {
                            tocaFondo = true;
                        } else {
                            if (tablero[fReal][cReal].getEstadoPixel() == EstadoPixel.NAVE) {
                                tocaNave = true;
                            }
                        }
                    }

                    if (tocaFondo == true) {
                        finPorDerrota = true;
                        e.setPosicionCentral(nuevaFila, nuevaCol);
                    } else {
                        if (tocaNave == true) {
                            finPorDerrota = true;
                            e.setPosicionCentral(nuevaFila, nuevaCol);
                        } else {
                            e.setPosicionCentral(nuevaFila, nuevaCol);
                        }
                    }

                    dibujarActor(e, EstadoPixel.ENEMIGO);
                }
            }

            if (finPorDerrota == true) {
                detenerTimers();
                estadoJuego.set("DERROTA");
            }
        }
    }

    private void moverDisparos() {
        Iterator<Disparo> iterator = listaDisparos.iterator();

        while (iterator.hasNext()) {
            Disparo d = iterator.next();

            // 1. Borramos la forma entera antigua
            dibujarActor(d, EstadoPixel.VACIO);

            int nuevaFila = d.getFilaCentral() - 1;
            int col = d.getColumnaCentral();

            // Comprobamos si la nueva posición entera se sale por arriba
            boolean salePorArriba = false;
            for (int[] delta : d.getForma()) {
                if (nuevaFila + delta[0] < 0) {
                    salePorArriba = true;
                }
            }

            if (salePorArriba == true) {
                iterator.remove();
            } else {
                boolean impacto = false;
                for (int[] delta : d.getForma()) {
                    int fReal = nuevaFila + delta[0];
                    int cReal = col + delta[1];
                    if (fReal < FILAS) {
                        if (tablero[fReal][cReal].getEstadoPixel() == EstadoPixel.ENEMIGO) {
                            impacto = true;
                        }
                    }
                }

                if (impacto == true) {
                    iterator.remove();

                    boolean enemigoEncontrado = false;
                    Iterator<Enemigo> itEnemigo = listaEnemigos.iterator();

                    // ARREGLO 2: Seguro anti-doble borrado
                    while (itEnemigo.hasNext() && enemigoEncontrado == false) {
                        Enemigo e = itEnemigo.next();

                        // ¿Algún pixel del disparo choca con algún pixel del enemigo?
                        for (int[] deltaDisp : d.getForma()) {
                            int fDisp = nuevaFila + deltaDisp[0];
                            int cDisp = col + deltaDisp[1];

                            for (int[] deltaEne : e.getForma()) {
                                int fEne = e.getFilaCentral() + deltaEne[0];
                                int cEne = e.getColumnaCentral() + deltaEne[1];

                                if (fDisp == fEne) {
                                    if (cDisp == cEne) {
                                        // Comprobamos que no lo hayamos borrado ya en este mismo chequeo
                                        if (enemigoEncontrado == false) {
                                            dibujarActor(e, EstadoPixel.VACIO);
                                            itEnemigo.remove();
                                            enemigoEncontrado = true;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (listaEnemigos.isEmpty()) {
                        detenerTimers();
                        estadoJuego.set("VICTORIA");
                    }
                } else {
                    d.setPosicionCentral(nuevaFila, col);
                    dibujarActor(d, EstadoPixel.DISPARO);
                }
            }
        }
    }

    public Pixel getPixel(int f, int c) {
        return tablero[f][c];
    }

    public void moverNaveIzquierda() {
        if (nave != null) {
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
                    detenerTimers();
                    estadoJuego.set("DERROTA");
                } else {
                    dibujarActor(nave, EstadoPixel.VACIO);
                    nave.setPosicionCentral(nave.getFilaCentral(), nave.getColumnaCentral() - 1);
                    dibujarActor(nave, EstadoPixel.NAVE);
                }
            }
        }
    }

    public void moverNaveDerecha() {
        if (nave != null) {
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
                    detenerTimers();
                    estadoJuego.set("DERROTA");
                } else {
                    dibujarActor(nave, EstadoPixel.VACIO);
                    nave.setPosicionCentral(nave.getFilaCentral(), nave.getColumnaCentral() + 1);
                    dibujarActor(nave, EstadoPixel.NAVE);
                }
            }
        }
    }

    public void moverNaveArriba() {
        if (nave != null) {
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
                    detenerTimers();
                    estadoJuego.set("DERROTA");
                } else {
                    dibujarActor(nave, EstadoPixel.VACIO);
                    nave.setPosicionCentral(nave.getFilaCentral() - 1, nave.getColumnaCentral());
                    dibujarActor(nave, EstadoPixel.NAVE);
                }
            }
        }
    }

    public void moverNaveAbajo() {
        if (nave != null) {
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
                    detenerTimers();
                    estadoJuego.set("DERROTA");
                } else {
                    dibujarActor(nave, EstadoPixel.VACIO);
                    nave.setPosicionCentral(nave.getFilaCentral() + 1, nave.getColumnaCentral());
                    dibujarActor(nave, EstadoPixel.NAVE);
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

    public SimpleStringProperty estadoJuegoProperty() {
        return estadoJuego;
    }

    public void disparar() {
        if (nave != null) {
            // 1. Buscamos cuál es el píxel más alto de la nave actual (el delta negativo más grande)
            int minDeltaNave = 0;
            for (int[] delta : nave.getForma()) {
                if (delta[0] < minDeltaNave) {
                    minDeltaNave = delta[0];
                }
            }

            // 2. Calculamos la fila real más alta de la nave en el tablero
            int filaMasAltaNave = nave.getFilaCentral() + minDeltaNave;

            // 3. El disparo debe apoyar su parte MÁS BAJA justo 1 píxel por encima del techo de la nave
            int filaFondoDisparo = filaMasAltaNave - 1;

            if (filaFondoDisparo >= 0) {
                estrategiaActual.realizarDisparo(filaFondoDisparo, nave.getColumnaCentral(), listaDisparos);

                if (listaDisparos.isEmpty() == false) {
                    Disparo nuevoDisparo = listaDisparos.get(listaDisparos.size() - 1);
                    dibujarActor(nuevoDisparo, EstadoPixel.DISPARO);
                }
            }
        }
    }

    public void cambiarArma() {
        if (tipoNaveSeleccionada.equals("GREEN")) {
            // GREEN solo alterna entre Único y Flecha
            if (estrategiaActual instanceof DisparoUnico) {
                estrategiaActual = new DisparoFlecha();
            } else {
                estrategiaActual = new DisparoUnico();
            }
        } else {
            if (tipoNaveSeleccionada.equals("BLUE")) {
                // BLUE solo alterna entre Único y Rombo
                if (estrategiaActual instanceof DisparoUnico) {
                    estrategiaActual = new DisparoRombo();
                } else {
                    estrategiaActual = new DisparoUnico();
                }
            } else {
                if (tipoNaveSeleccionada.equals("RED")) {
                    // RED rota entre los tres: Único -> Flecha -> Rombo -> Único...
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
}