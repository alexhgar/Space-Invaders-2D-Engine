package alexjulenerik.demo.controller;

import alexjulenerik.demo.model.GameModel;
import alexjulenerik.demo.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InicioController implements Initializable {

    @FXML
    private Button btnJugar;

    @FXML
    private Button btnGreen;

    @FXML
    private Button btnBlue;

    @FXML
    private Button btnRed;

    private String tipoNaveElegida = "GREEN";

    private static final GameModel modelo = GameModel.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        // Al arrancar, marcamos el botón GREEN como seleccionado
        actualizarEstilos();
    }


    @FXML
    public void seleccionarGreen(ActionEvent event){
        tipoNaveElegida = "GREEN";
        actualizarEstilos();
    }

    @FXML
    public void seleccionarBlue(ActionEvent event){
        tipoNaveElegida = "BLUE";
        actualizarEstilos();
    }

    @FXML
    public void seleccionarRed(ActionEvent event){
        tipoNaveElegida = "RED";
        actualizarEstilos();
    }

    private void actualizarEstilos(){
        // 1. EL RESET: Devolvemos todos los botones a su estado normal (gris y plano)
        String estiloApagado = "-fx-background-color: #D3D3D3; -fx-translate-y: 0; -fx-effect: null; -fx-cursor: hand;";
        btnGreen.setStyle(estiloApagado);
        btnBlue.setStyle(estiloApagado);
        btnRed.setStyle(estiloApagado);
        //Aplicamos el efecto "hundido" y color al seleccionado
        String estiloActivo = "-fx-translate-y: 0; -fx-cursor: hand; -fx-font-weight: bold; -fx-text-fill: white; -fx-border-width: 2px; -fx-border-radius: 3px; -fx-background-radius: 3px;";
        switch (tipoNaveElegida) {
            case "GREEN" -> {
                btnGreen.setStyle(estiloActivo + "-fx-background-color: #32CD32; -fx-border-color: #00FF00;");
            }
            case "BLUE"  -> {
                btnBlue.setStyle(estiloActivo + "-fx-background-color: #1E90FF; -fx-border-color: #00BFFF;");
            }
            case "RED"   -> {
                btnRed.setStyle(estiloActivo + "-fx-background-color: #FF0000; -fx-border-color: #FF4500;");
            }
        }
    }


    @FXML
    public void onJugarClick(ActionEvent event) {
        try {
            // llama a viewfactory y carga la pantalla del juego
            ViewFactory.mostrarPantallaJuego();

            Stage stage = (Stage) btnJugar.getScene().getWindow();

            if (stage != null) {
                stage.close();
            } else {
                System.out.println("Error: No se ha podido encontrar la ventana de inicio para cerrarla.");
            }

        } catch (IOException e) {
            System.out.println("Error al cargar la pantalla de juego.");
            e.printStackTrace();
        }
    }
}
