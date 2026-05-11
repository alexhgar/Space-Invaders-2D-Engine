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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

//ERIK
public class GameController implements Initializable {
    @FXML
    private GridPane pnlGame;
    @FXML
    private BorderPane pnlMain;

    //Añadimos la referencia a la etiqueta de puntos del FXML
    @FXML
    private Label lblPuntos;

    private static final GameModel modelo = GameModel.getInstance();


    public void initialize (URL url, ResourceBundle resources){
        //Dibuja la matriz de pixeles e inicializa la partida
        for (int fila = 0; fila<GameModel.FILAS; fila++){
            for(int columna = 0; columna<GameModel.COLUMNAS; columna++){
                pnlGame.add(crearPixel(fila,columna),columna,fila);
            }
        }

        // Vinculamos la etiqueta con la puntuación del modelo
        // Esto hace que el texto cambie solo cada vez que el modelo actualiza los puntos
        lblPuntos.textProperty().bind(modelo.puntuacionVisualProperty().asString());

        modelo.estadoJuegoProperty().addListener((observable, viejoEstado, nuevoEstado) -> {
            if (nuevoEstado.equals("VICTORIA")) {
                Platform.runLater(() -> {
                    Stage stage = (Stage) pnlMain.getScene().getWindow();
                    ViewFactory.mostrarAlertaFin("Victoria", "Has derrotado a los enemigos", stage);
                });
            } else {
                if (nuevoEstado.equals("DERROTA")) {
                    Platform.runLater(() -> {
                        Stage stage = (Stage) pnlMain.getScene().getWindow();
                        ViewFactory.mostrarAlertaFin("Derrota", "Suerte la proxima vez", stage);
                    });
                }
            }
        });
        modelo.inicializarPartida();

        Platform.runLater(() -> {
            pnlMain.requestFocus();
        });
    }

    private Node crearPixel(int fila, int columna){
        Rectangle rectangulo = new Rectangle(10,10);
        var pixel = modelo.getPixel(fila,columna);
        setPixelColor(rectangulo,pixel);
        pixel.estadoProperty().addListener((observable, viejoEstado, nuevoEstado) -> {
            setPixelColor(rectangulo, pixel);});
        return rectangulo;
    }

    private void setPixelColor(Rectangle rect, Pixel pixel){
        var estado = pixel.getEstadoPixel();
        switch(estado){
            case NAVE -> {
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