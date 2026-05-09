package alexjulenerik.demo;

import alexjulenerik.demo.view.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ViewFactory.mostrarPantallaInicio();
    }
}
