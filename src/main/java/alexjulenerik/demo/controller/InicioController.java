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

        /*    // Si el jugador no pone nada o pone solo espacios, le llamamos "Invitado"
            if (nombre == null || nombre.trim().isEmpty()) {
                modelo.setNombreJugador("Invitado");
            } else {
                modelo.setNombreJugador(nombre);
            }
        */
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

    @FXML
    public void onVerRankingClick(ActionEvent event) {
        // Comentario temporal
        System.out.println("Botón ranking pulsado. En stand by hasta actualizar ViewFactory.");

        /* try {
            ViewFactory.mostrarPantallaRanking();

            Stage stage = (Stage) btnRanking.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
