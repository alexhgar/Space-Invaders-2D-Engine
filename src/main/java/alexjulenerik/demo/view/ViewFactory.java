package alexjulenerik.demo.view;
import alexjulenerik.demo.GameApplication;
import alexjulenerik.demo.controller.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
//ERIK
public class ViewFactory {
    public static void mostrarPantallaInicio() throws IOException{
        FXMLLoader fmxlLoader = new FXMLLoader(GameApplication.class.getResource("inicio-view.fxml"));
        //Establecer tamaño del menu
        Scene scene = new Scene(fmxlLoader.load(),600,400);
        //Crear el stage
        Stage stage = new Stage();
        stage.setTitle("Inicio");
        stage.setScene(scene);
        stage.show();
    }

    public static void mostrarPantallaJuego() throws IOException{
        //Cargar el gameview
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("gameview.fxml"));
        //Tamaño 600x1000 ya que 60x100 se veria muy pequeño (mismas proporciones)
        Scene scene = new Scene(fxmlLoader.load(),1040,670 );
        //Crear el stage
        Stage stage = new Stage();
        stage.setTitle("Space Invaders");
        stage.setScene(scene);
        stage.show();
    }

    public static void mostrarPantallaRanking() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("ranking.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),600,400);
        Stage stage = new Stage();
        stage.setTitle("Ranking");
        stage.setScene(scene);
        stage.show();
    }

    public static void mostrarAlertaFin(String titulo, String mensaje, Stage stageActual) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Fin de la partida");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();

        // En lugar de salir, cerramos la ventana de juego
        if (stageActual != null) {
            stageActual.close();
        }
        try {
            mostrarPantallaInicio();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
