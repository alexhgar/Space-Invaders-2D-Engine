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
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        // Hacemos que las celdas sean blancas y sin fondo
        listaRanking.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setBackground(Background.EMPTY);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                    setFont(Font.font("System", FontWeight.BOLD, 16));
                }
            }
        });
        // Pedimos los datos al modelo y añadimos la posición
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