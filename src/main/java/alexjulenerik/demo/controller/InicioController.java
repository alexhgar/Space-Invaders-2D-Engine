package alexjulenerik.demo.controller;

import alexjulenerik.demo.model.GameModel;
import alexjulenerik.demo.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InicioController {

    @FXML
    private Button btnJugar;

    @FXML
    private ToggleButton btnGreen;

    @FXML
    private ToggleButton btnBlue;

    @FXML
    private ToggleButton btnRed;

    private String tipoNaveElegida = "GREEN";

    private static final GameModel modelo = GameModel.getInstance();




    @FXML
    public void seleccionarGreen(ActionEvent event){
        tipoNaveElegida = "GREEN";
    }

    @FXML
    public void seleccionarBlue(ActionEvent event){
        tipoNaveElegida = "BLUE";
    }

    @FXML
    public void seleccionarRed(ActionEvent event){
        tipoNaveElegida = "RED";
    }



    @FXML
    public void onJugarClick(ActionEvent event) {
        try {

            modelo.setTipoNaveSeleccionada(tipoNaveElegida);
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
