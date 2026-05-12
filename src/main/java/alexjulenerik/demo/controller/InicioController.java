package alexjulenerik.demo.controller;

import alexjulenerik.demo.model.GameModel;
import alexjulenerik.demo.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

    @FXML
    private TextField campoNombre;

    @FXML
    private Button btnRanking;

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
            // Capturar el nombre del jugador
            String nombre = campoNombre.getText();

            // Si el jugador no pone nada o pone solo espacios, le llamamos "Invitado
            if (nombre == null||nombre.isEmpty()) {
                modelo.setNombreJugador("Invitado");
            } else {
                modelo.setNombreJugador(nombre);
            }

            modelo.setTipoNaveSeleccionada(tipoNaveElegida);
            ViewFactory.mostrarPantallaJuego();
            Stage stage = (Stage) btnJugar.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onVerRankingClick(ActionEvent event) {

         try {
            ViewFactory.mostrarPantallaRanking();
            Stage stage = (Stage) btnRanking.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
