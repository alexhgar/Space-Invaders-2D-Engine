package alexjulenerik.demo.controller;

import alexjulenerik.demo.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class RankingController {
    @FXML
    public Button btnVolver;

    @FXML
    public void onVolverClick(ActionEvent event) {
        try {
            // Volvemos a la pantalla de inicio
            ViewFactory.mostrarPantallaInicio();

            // Cerramos la pantalla del ranking actual
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }else{
                System.out.println("Error: No se ha podido encontrar la ventana del ranking para cerrarla.");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la pantalla de inicio.");
            e.printStackTrace();
        }
    }
}
