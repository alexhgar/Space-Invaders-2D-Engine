package alexjulenerik.demo.controller;

import alexjulenerik.demo.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class InicioController {

    @FXML
    private Button btnJugar;


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
