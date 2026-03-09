package alexjulenerik.demo.controller;

import alexjulenerik.demo.model.GameModel;
import alexjulenerik.demo.model.Pixel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

//ERIK
public class GameController {
    @FXML
    //Enlazar los paneles de gameview.fxml
    private GridPane pnlGame;
    @FXML
    private BorderPane pnlMain;

    private static final GameModel modelo = GameModel.getInstance();

    @FXML
    public void initialize(){
        //Dibuja la matriz de pixeles e inicializa la partida
        for (int fila = 0; fila<GameModel.FILAS; fila++){
            for(int columna = 0; columna<GameModel.COLUMNAS; columna++){
                pnlGame.add(crearPixel(fila,columna),columna,fila);
            }
        }
        modelo.inicializarPartida();
    }

    private Node crearPixel(int fila, int columna){
        //Crea cada pixel individual
        Rectangle rectangulo = new Rectangle();
        var pixel = modelo.getPixel(fila,columna);
        setPixelColor(rectangulo,pixel);
        pixel.estadoProperty().addListener(observable -> pixel.setEstadoPixel(pixel.getEstadoPixel()));
        return rectangulo;
    }

    private void setPixelColor(Rectangle rect, Pixel pixel){
        //Cambia el estado del pixel dependiendo de que actor se encuentra en él
        var estado = pixel.getEstadoPixel();
        switch(estado){
            case NAVE -> rect.setFill(Color.GREEN);
            case ENEMIGO -> rect.setFill(Color.RED);
            case DISPARO -> rect.setFill(Color.YELLOW);
            case VACIO -> rect.setFill(Color.BLACK);
        }
    }

    public void controles(KeyEvent evento){
        // gestor de eventos que se llama cuando se pulsa una tecla
        // y que llama a los metodos de movimiento del modelo
        switch (evento.getCode()){
            //case W -> modelo.moverNaveArriba();
            //case S -> modelo.moverNaveAbajo();
            case D -> modelo.moverNaveDerecha();
            case A -> modelo.moverNaveIzquierda();
            case SPACE -> modelo.disparar();
        }
    }
}
