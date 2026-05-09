package alexjulenerik.demo.controller;

import alexjulenerik.demo.model.GameModel;
import alexjulenerik.demo.model.Puntuacion;
import alexjulenerik.demo.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RankingController implements Initializable {

    @FXML
    public Button btnVolver;

    @FXML
    public ListView<String> listaRanking;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // 1. Truco visual: Forzamos a que las celdas sean blancas, grandes y sin fondo
        listaRanking.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty == true) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    if (item == null) {
                        setText(null);
                        setStyle("-fx-background-color: transparent;");
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
                    }
                }
            }
        });

        // 2. Pedimos los datos al modelo y añadimos la posición
        int posicion = 1;
        for (Puntuacion p : GameModel.getInstance().getTopRanking()) {
            listaRanking.getItems().add(posicion + ". " + p.toString());
            posicion++;
        }
    }

    @FXML
    public void onVolverClick(ActionEvent event) {
        try {
            ViewFactory.mostrarPantallaInicio();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}