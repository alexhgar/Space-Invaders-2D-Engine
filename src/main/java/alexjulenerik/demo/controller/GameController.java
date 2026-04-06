package alexjulenerik.demo.controller;

import alexjulenerik.demo.model.GameModel;
import alexjulenerik.demo.model.Pixel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import alexjulenerik.demo.view.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

//ERIK
public class GameController implements Initializable { //Initializable se asegura de que las variables fmxl no son nulas
    //Enlazar los paneles de gameview.fxml
    @FXML
    private GridPane pnlGame;
    @FXML
    private BorderPane pnlMain;

    private static final GameModel modelo = GameModel.getInstance();


    public void initialize (URL url, ResourceBundle resources){
        //Dibuja la matriz de pixeles e inicializa la partida
        for (int fila = 0; fila<GameModel.FILAS; fila++){
            for(int columna = 0; columna<GameModel.COLUMNAS; columna++){
                pnlGame.add(crearPixel(fila,columna),columna,fila);
            }
        }
        modelo.estadoJuegoProperty().addListener((observable, viejoEstado, nuevoEstado) -> {
            if (nuevoEstado.equals("VICTORIA")) {
                Platform.runLater(() -> ViewFactory.mostrarAlertaFin("Victoria", "Has derrotado a los enemigos"));
            } else {
                if (nuevoEstado.equals("DERROTA")) {
                    Platform.runLater(() -> ViewFactory.mostrarAlertaFin("Derrota", "Suerte la proxima vez"));
                }
            }
        });
        modelo.inicializarPartida();
        //javafx empieza a escuchar las teclas pulsadas por teclado
        Platform.runLater(() -> {
            pnlMain.requestFocus();
        });
    }

    //Metodo para crear el Pixel. Este metodo no se puede hacer en la clase Pixel ya que rompería el MVC.
    private Node crearPixel(int fila, int columna){
        //Crea cada pixel individual
        Rectangle rectangulo = new Rectangle(10,10);
        var pixel = modelo.getPixel(fila,columna);
        setPixelColor(rectangulo,pixel);
        pixel.estadoProperty().addListener((observable, viejoEstado, nuevoEstado) -> {
            setPixelColor(rectangulo, pixel);});
        return rectangulo;
    }

    //Metodo para definir el color del Pixel. Este metodo no se puede hacer en la clase Pixel ya que rompería el MVC.
    private void setPixelColor(Rectangle rect, Pixel pixel){
        var estado = pixel.getEstadoPixel();
        switch(estado){
            case NAVE -> {
                // Leemos el tipo de nave para asignarle su color
                String tipo = modelo.getTipoNaveSeleccionada();
                if (tipo.equals("BLUE")) {
                    rect.setFill(Color.BLUE);
                } else {
                    if (tipo.equals("RED")) {
                        rect.setFill(Color.RED);
                    } else {
                        rect.setFill(Color.GREEN);
                    }
                }
            }
            case ENEMIGO -> rect.setFill(Color.RED);
            case DISPARO -> rect.setFill(Color.WHITE);
            case VACIO -> rect.setFill(Color.TRANSPARENT);
        }
    }

    public void controles(KeyEvent evento){
        // gestor de eventos que se llama cuando se pulsa una tecla
        // y que llama a los metodos de movimiento del modelo
        switch (evento.getCode()){
            case W -> modelo.moverNaveArriba();
            case S -> modelo.moverNaveAbajo();
            case D -> modelo.moverNaveDerecha();
            case A -> modelo.moverNaveIzquierda();
            case SPACE -> modelo.disparar();
            case M -> modelo.cambiarArma();
        }
    }
}
